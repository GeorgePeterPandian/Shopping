package com.shopping.products.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val products: List<ProductResponseItem>,
    val total: Int,
    val skip: Int,
    val limit: Int
)