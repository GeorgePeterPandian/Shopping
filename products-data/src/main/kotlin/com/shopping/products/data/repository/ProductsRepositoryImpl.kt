package com.shopping.products.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.shopping.products.data.datasource.ProductsRemoteDataSource
import com.shopping.products.data.local.dao.ProductDao
import com.shopping.products.data.local.entity.ProductEntity
import com.shopping.products.data.mapper.toDomain
import com.shopping.products.data.mapper.toEntity
import com.shopping.products.domain.model.Product
import com.shopping.products.domain.repository.ProductsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductsRemoteDataSource, private val productDao: ProductDao
) : ProductsRepository {

    private val refreshMutex = Mutex()

    override fun getProducts(): Flow<PagingData<Product>> =
        pagedProducts { productDao.pagingSource() }

    override fun searchProducts(query: String): Flow<PagingData<Product>> =
        pagedProducts { productDao.searchPagingSource(query.trim()) }


    private fun pagedProducts(
        pagingSourceFactory: () -> PagingSource<Int, ProductEntity>
    ): Flow<PagingData<Product>> = flow {
        try {
            refreshIfStale()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            val cachedProductCount = productDao.getProductCount()
            if (cachedProductCount == 0) throw exception
        }

        emitAll(
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow.map { pagingData ->
                pagingData.map { entity -> entity.toDomain() }
            }
        )
    }


    override suspend fun getProduct(id: Int): Product {
        val product = remoteDataSource.getProduct(id).toDomain()
        // Preserve the listing cache timestamp when this product is already cached.
        // A detail request must not make the entire listing cache appear fresh.
        val cachedAt = productDao.getProductCacheTimestamp(id) ?: 0L
        productDao.upsertProducts(listOf(product.toEntity(cachedAt = cachedAt)))
        return product
    }

    private suspend fun refreshIfStale() {
        refreshMutex.withLock {
            val now = System.currentTimeMillis()
            val productCount = productDao.getProductCount()
            val lastCachedAt = productDao.getLatestCacheTimestamp()
            val isStale =
                productCount == 0 || lastCachedAt == null || now - lastCachedAt >= CACHE_TTL_MILLIS

            if (!isStale) return
            refreshAllProducts()
        }
    }

    private suspend fun refreshAllProducts() {
        val fetchedProducts = mutableListOf<Product>()
        var skip = 0
        var total = Int.MAX_VALUE
        while (skip < total) {
            val response = remoteDataSource.getProducts(limit = PAGE_SIZE, skip = skip)
            total = response.total
            val products = response.products.map { it.toDomain() }
            if (products.isEmpty()) break
            fetchedProducts += products
            skip += products.size
        }

        if (fetchedProducts.isNotEmpty()) {
            val cachedAt = System.currentTimeMillis()
            productDao.upsertProducts(
                fetchedProducts.map { it.toEntity(cachedAt) })
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
        const val CACHE_TTL_MILLIS = 30 * 60 * 1000L
    }
}
