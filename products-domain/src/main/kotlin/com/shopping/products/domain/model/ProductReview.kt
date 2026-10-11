package com.shopping.products.domain.model

data class ProductReview(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)