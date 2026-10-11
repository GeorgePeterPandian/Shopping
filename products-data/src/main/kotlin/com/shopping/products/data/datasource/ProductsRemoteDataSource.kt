package com.shopping.products.data.datasource

import com.shopping.products.data.model.ProductResponse
import com.shopping.products.data.model.ProductResponseItem

interface ProductsRemoteDataSource {
    suspend fun getProducts(limit: Int, skip: Int): ProductResponse

    suspend fun searchProducts(query: String, limit: Int, skip: Int): ProductResponse

    suspend fun getProduct(id: Int): ProductResponseItem
}