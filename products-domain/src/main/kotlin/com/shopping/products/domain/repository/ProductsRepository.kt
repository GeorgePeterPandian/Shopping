package com.shopping.products.domain.repository

import androidx.paging.PagingData
import com.shopping.products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun getProducts(): Flow<PagingData<Product>>

    fun searchProducts(query: String): Flow<PagingData<Product>>
    suspend fun getProduct(id: Int): Product
}
