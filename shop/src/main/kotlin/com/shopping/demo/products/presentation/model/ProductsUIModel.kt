package com.shopping.demo.products.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProductsUIModel(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val tags: List<String>,
    val brand: String?,
    val sku: String,
    val weight: Double,
    val dimensions: ProductDimensionsUIModel,
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val reviews: List<ProductReviewUIModel>,
    val returnPolicy: String,
    val minimumOrderQuantity: Int,
    val images: List<String>,
    val thumbnail: String,
    val discountedPrice: Double
)

@Immutable
data class ProductDimensionsUIModel(
    val width: Double,
    val height: Double,
    val depth: Double
)

@Immutable
data class ProductReviewUIModel(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)