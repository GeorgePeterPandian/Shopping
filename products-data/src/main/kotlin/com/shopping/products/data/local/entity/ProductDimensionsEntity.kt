package com.shopping.products.data.local.entity

import kotlinx.serialization.Serializable

@Serializable
data class ProductDimensionsEntity(
    val width: Double,
    val height: Double,
    val depth: Double
)
