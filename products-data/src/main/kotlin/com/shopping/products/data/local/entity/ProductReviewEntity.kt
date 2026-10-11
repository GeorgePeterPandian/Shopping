package com.shopping.products.data.local.entity

import kotlinx.serialization.Serializable

@Serializable
data class ProductReviewEntity(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)
