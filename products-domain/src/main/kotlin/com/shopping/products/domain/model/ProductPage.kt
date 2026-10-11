package com.shopping.products.domain.model

data class ProductPage(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)