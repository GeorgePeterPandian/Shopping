package com.shopping.products.data.repository

import androidx.paging.PagingSource
import com.shopping.products.data.datasource.ProductsRemoteDataSource
import com.shopping.products.data.local.dao.ProductDao
import com.shopping.products.data.local.entity.ProductEntity
import com.shopping.products.data.model.ProductDimensionsResponse
import com.shopping.products.data.model.ProductMetaResponse
import com.shopping.products.data.model.ProductResponseItem
import com.shopping.products.data.model.ProductReviewResponse
import com.shopping.products.domain.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ProductsRepositoryTest {

    private lateinit var remoteDataSource: ProductsRemoteDataSource
    private lateinit var productDao: ProductDao
    private lateinit var repository: ProductsRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk(relaxed = true)
        productDao = mockk(relaxed = true)

        repository = ProductsRepositoryImpl(
            remoteDataSource = remoteDataSource,
            productDao = productDao
        )
    }

    @Test
    fun `getProducts should emit PagingData`() = runTest {
        every {
            productDao.pagingSource()
        } returns emptyPagingSource()

        val result = repository.getProducts().first()

        assertNotNull(result)
    }

    @Test
    fun `searchProducts should emit PagingData`() = runTest {
        every {
            productDao.searchPagingSource("phone")
        } returns emptyPagingSource()

        val result = repository.searchProducts("phone").first()

        assertNotNull(result)
    }

    @Test
    fun `getProduct should return product from remote source`() = runTest {
        coEvery {
            remoteDataSource.getProduct(1)
        } returns productResponse()

        val result = repository.getProduct(1)

        assertNotNull(result)
        assertProductFields(result)

        coVerify(exactly = 1) {
            remoteDataSource.getProduct(1)
        }
    }

    private fun assertProductFields(product: Product) {
        org.junit.Assert.assertEquals(1, product.id)
        org.junit.Assert.assertEquals("iPhone", product.title)
        org.junit.Assert.assertEquals("Apple phone", product.description)
        org.junit.Assert.assertEquals("smartphones", product.category)
        org.junit.Assert.assertEquals(999.99, product.price, 0.001)
        org.junit.Assert.assertEquals(10.5, product.discountPercentage, 0.001)
        org.junit.Assert.assertEquals(4.5, product.rating, 0.001)
        org.junit.Assert.assertEquals(50, product.stock)
        org.junit.Assert.assertEquals("Apple", product.brand)
    }

    private fun emptyPagingSource(): PagingSource<Int, ProductEntity> {
        return object : PagingSource<Int, ProductEntity>() {
            override fun getRefreshKey(
                state: androidx.paging.PagingState<Int, ProductEntity>
            ): Int? = null

            override suspend fun load(
                params: LoadParams<Int>
            ): LoadResult<Int, ProductEntity> {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        }
    }

    private fun productResponse() = ProductResponseItem(
        id = 1,
        title = "iPhone",
        description = "Apple phone",
        category = "smartphones",
        price = 999.99,
        discountPercentage = 10.5,
        rating = 4.5,
        stock = 50,
        tags = listOf("smartphones", "apple"),
        brand = "Apple",
        sku = "IPHONE-001",
        weight = 200.0,
        dimensions = ProductDimensionsResponse(
            width = 7.1,
            height = 14.7,
            depth = 0.8
        ),
        warrantyInformation = "1 year warranty",
        shippingInformation = "Ships in 2 days",
        availabilityStatus = "In Stock",
        reviews = listOf(
            ProductReviewResponse(
                rating = 5,
                comment = "Excellent phone",
                date = "2026-01-01T00:00:00.000Z",
                reviewerName = "John Doe",
                reviewerEmail = "john@example.com"
            )
        ),
        returnPolicy = "30 days return",
        minimumOrderQuantity = 1,
        meta = ProductMetaResponse(
            createdAt = "2026-01-01T00:00:00.000Z",
            updatedAt = "2026-01-02T00:00:00.000Z",
            barcode = "123456789",
            qrCode = "https://example.com/qr"
        ),
        images = listOf("image.jpg"),
        thumbnail = "image.jpg"
    )
}