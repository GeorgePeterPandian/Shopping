package com.shopping.products.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductMetaResponse(
    val createdAt: String,
    val updatedAt: String,
    val barcode: String,
    val qrCode: String
)