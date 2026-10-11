package com.shopping.products.data.datasource

import com.shopping.products.data.model.ProductResponse
import com.shopping.products.data.model.ProductResponseItem
import com.shopping.products.data.service.ProductsApi
import javax.inject.Inject

class ProductsRemoteDataSourceImpl @Inject constructor(private val api: ProductsApi) :
    ProductsRemoteDataSource {

    override suspend fun getProducts(limit: Int, skip: Int): ProductResponse = api.getProducts(
        limit = limit, skip = skip
    )

    override suspend fun searchProducts(query: String, limit: Int, skip: Int): ProductResponse =
        api.searchProducts(query = query, limit = limit, skip = skip)

    override suspend fun getProduct(id: Int): ProductResponseItem = api.getProduct(id)
}