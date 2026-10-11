package com.shopping.products.data.mapper

import com.shopping.products.data.model.ProductResponseItem
import com.shopping.products.domain.model.Product
import com.shopping.products.domain.model.ProductDimensions
import com.shopping.products.domain.model.ProductReview

fun ProductResponseItem.toDomain(): Product = Product(
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
    dimensions = ProductDimensions(
        width = dimensions.width, height = dimensions.height, depth = dimensions.depth
    ),
    warrantyInformation = warrantyInformation,
    shippingInformation = shippingInformation,
    availabilityStatus = availabilityStatus,
    reviews = reviews.map { review ->
        ProductReview(
            rating = review.rating,
            comment = review.comment,
            date = review.date,
            reviewerName = review.reviewerName,
            reviewerEmail = review.reviewerEmail
        )
    },
    returnPolicy = returnPolicy,
    minimumOrderQuantity = minimumOrderQuantity,
    images = images,
    thumbnail = thumbnail
)