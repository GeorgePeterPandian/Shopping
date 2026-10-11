package com.shopping.products.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shopping.products.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY id ASC")
    fun pagingSource(): PagingSource<Int, ProductEntity>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun observeProduct(productId: Int): Flow<ProductEntity?>

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    @Query("SELECT MAX(cachedAt) FROM products")
    suspend fun getLatestCacheTimestamp(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProducts(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clearProducts()

    @Query(
        """
    SELECT * FROM products
    WHERE title LIKE '%' || :query || '%'
       OR category LIKE '%' || :query || '%'
       OR brand LIKE '%' || :query || '%'
    ORDER BY id ASC
    """
    )
    fun searchPagingSource(query: String): PagingSource<Int, ProductEntity>

    @Query("SELECT cachedAt FROM products WHERE id = :productId")
    suspend fun getProductCacheTimestamp(productId: Int): Long?
}
