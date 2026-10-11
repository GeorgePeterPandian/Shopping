package com.shopping.products.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductReviewResponse(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)
