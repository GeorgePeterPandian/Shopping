package com.shopping.products.domain.model

data class Product(
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
    val dimensions: ProductDimensions,
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val reviews: List<ProductReview>,
    val returnPolicy: String,
    val minimumOrderQuantity: Int,
    val images: List<String>,
    val thumbnail: String,
){
    val discountedPrice: Double
        get() {
            val discount = discountPercentage.coerceIn(0.0, 100.0)
            return kotlin.math.round(
                price * (1.0 - discount / 100.0) * 100.0
            ) / 100.0
        }

}