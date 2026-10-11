package com.shopping.demo.products.presentation.model

import com.shopping.products.domain.model.Product

fun Product.toUIModel(): ProductsUIModel =
    ProductsUIModel(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags.toList(),
        brand = brand,
        sku = sku,
        weight = weight,
        dimensions = ProductDimensionsUIModel(
            width = dimensions.width,
            height = dimensions.height,
            depth = dimensions.depth
        ),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = reviews.map { review ->
            ProductReviewUIModel(
                rating = review.rating,
                comment = review.comment,
                date = review.date,
                reviewerName = review.reviewerName,
                reviewerEmail = review.reviewerEmail
            )
        },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        images = images.toList(),
        thumbnail = thumbnail,
        discountedPrice = discountedPrice
    )