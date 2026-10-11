package com.shopping.products.data.service

import com.shopping.products.data.model.ProductResponse
import com.shopping.products.data.model.ProductResponseItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int, @Query("skip") skip: Int
    ): ProductResponse

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductResponse

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int
    ): ProductResponseItem
}