package com.shopping.products.data.mapper

import com.shopping.products.data.local.entity.ProductDimensionsEntity
import com.shopping.products.data.local.entity.ProductEntity
import com.shopping.products.data.local.entity.ProductReviewEntity
import com.shopping.products.domain.model.Product
import com.shopping.products.domain.model.ProductDimensions
import com.shopping.products.domain.model.ProductReview

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    title = title,
    description = description,
    category = category,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    stock = stock,
    tags = tags,
    brand = brand,
    sku = sku,
    weight = weight,
    dimensions = dimensions.toDomain(),
    warrantyInformation = warrantyInformation,
    shippingInformation = shippingInformation,
    availabilityStatus = availabilityStatus,
    reviews = reviews.map { it.toDomain() },
    returnPolicy = returnPolicy,
    minimumOrderQuantity = minimumOrderQuantity,
    images = images,
    thumbnail = thumbnail
)

fun Product.toEntity(cachedAt: Long): ProductEntity = ProductEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    stock = stock,
    tags = tags,
    brand = brand,
    sku = sku,
    weight = weight,
    dimensions = dimensions.toEntity(),
    warrantyInformation = warrantyInformation,
    shippingInformation = shippingInformation,
    availabilityStatus = availabilityStatus,
    reviews = reviews.map { it.toEntity() },
    returnPolicy = returnPolicy,
    minimumOrderQuantity = minimumOrderQuantity,
    images = images,
    thumbnail = thumbnail,
    cachedAt = cachedAt
)

private fun ProductDimensionsEntity.toDomain() = ProductDimensions(
    width = width, height = height, depth = depth
)

private fun ProductDimensions.toEntity() = ProductDimensionsEntity(
    width = width, height = height, depth = depth
)

private fun ProductReviewEntity.toDomain() = ProductReview(
    rating = rating,
    comment = comment,
    date = date,
    reviewerName = reviewerName,
    reviewerEmail = reviewerEmail
)

private fun ProductReview.toEntity() = ProductReviewEntity(
    rating = rating,
    comment = comment,
    date = date,
    reviewerName = reviewerName,
    reviewerEmail = reviewerEmail
)
