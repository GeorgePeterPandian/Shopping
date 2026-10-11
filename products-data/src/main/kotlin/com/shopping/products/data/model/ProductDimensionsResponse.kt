package com.shopping.products.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductDimensionsResponse(
    val width: Double,
    val height: Double,
    val depth: Double
)