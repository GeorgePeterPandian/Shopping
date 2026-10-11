package com.shopping.products.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponseItem(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val tags: List<String> = emptyList(),
    val brand: String? = null,
    val sku: String,
    val weight: Double,
    val dimensions: ProductDimensionsResponse,
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val reviews: List<ProductReviewResponse> = emptyList(),
    val returnPolicy: String,
    val minimumOrderQuantity: Int,
    val meta: ProductMetaResponse? = null,
    val images: List<String> = emptyList(),
    val thumbnail: String
)
