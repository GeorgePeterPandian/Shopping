package com.shopping.products.data.mapper

import com.shopping.products.data.local.entity.ProductDimensionsEntity
import com.shopping.products.data.local.entity.ProductEntity
import com.shopping.products.data.local.entity.ProductReviewEntity
import com.shopping.products.domain.model.Product
import com.shopping.products.domain.model.ProductDimensions
import com.shopping.products.domain.model.ProductReview
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductEntityMapperTest {

    @Test
    fun `toDomain maps entity fields correctly`() {
        val entity = createEntity()

        val result = entity.toDomain()

        assertEquals(entity.id, result.id)
        assertEquals(entity.title, result.title)
        assertEquals(entity.price, result.price, 0.001)
        assertEquals(entity.discountPercentage, result.discountPercentage, 0.001)
        assertEquals(entity.tags, result.tags)
        assertEquals(entity.dimensions.width, result.dimensions.width, 0.001)
        assertEquals(entity.dimensions.height, result.dimensions.height, 0.001)
        assertEquals(entity.dimensions.depth, result.dimensions.depth, 0.001)
        assertEquals(entity.reviews.size, result.reviews.size)
        assertEquals(entity.reviews.first().comment, result.reviews.first().comment)
    }

    @Test
    fun `toEntity maps product fields and cache timestamp correctly`() {
        val product = createProduct()
        val cachedAt = 1_791_234_567_890L

        val result = product.toEntity(cachedAt)

        assertEquals(product.id, result.id)
        assertEquals(product.title, result.title)
        assertEquals(product.price, result.price, 0.001)
        assertEquals(product.discountPercentage, result.discountPercentage, 0.001)
        assertEquals(product.tags, result.tags)
        assertEquals(product.dimensions.width, result.dimensions.width, 0.001)
        assertEquals(product.reviews.size, result.reviews.size)
        assertEquals(product.reviews.first().comment, result.reviews.first().comment)
        assertEquals(cachedAt, result.cachedAt)
    }

    @Test
    fun `entity to domain to entity preserves product data`() {
        val entity = createEntity()

        val result = entity.toDomain().toEntity(entity.cachedAt)

        assertEquals(entity.id, result.id)
        assertEquals(entity.title, result.title)
        assertEquals(entity.price, result.price, 0.001)
        assertEquals(entity.discountPercentage, result.discountPercentage, 0.001)
        assertEquals(entity.tags, result.tags)
        assertEquals(entity.images, result.images)
        assertEquals(entity.dimensions, result.dimensions)
        assertEquals(entity.reviews, result.reviews)
        assertEquals(entity.cachedAt, result.cachedAt)
    }

    private fun createEntity() = ProductEntity(
        id = 1,
        title = "Test Product",
        description = "Product description",
        category = "electronics",
        price = 100.0,
        discountPercentage = 10.0,
        rating = 4.5,
        stock = 20,
        tags = listOf("phone", "electronics"),
        brand = "Test Brand",
        sku = "TEST-001",
        weight = 1.5,
        dimensions = ProductDimensionsEntity(
            width = 10.0,
            height = 20.0,
            depth = 5.0
        ),
        warrantyInformation = "1 year",
        shippingInformation = "Standard shipping",
        availabilityStatus = "In Stock",
        reviews = listOf(
            ProductReviewEntity(
                rating = 5,
                comment = "Great product",
                date = "2026-01-01T10:00:00.000Z",
                reviewerName = "Test Reviewer",
                reviewerEmail = "reviewer@example.com"
            )
        ),
        returnPolicy = "30 days",
        minimumOrderQuantity = 1,
        images = listOf("image1.jpg", "image2.jpg"),
        thumbnail = "thumbnail.jpg",
        cachedAt = 1_791_234_567_890L
    )

    private fun createProduct() = Product(
        id = 1,
        title = "Test Product",
        description = "Product description",
        category = "electronics",
        price = 100.0,
        discountPercentage = 10.0,
        rating = 4.5,
        stock = 20,
        tags = listOf("phone", "electronics"),
        brand = "Test Brand",
        sku = "TEST-001",
        weight = 1.5,
        dimensions = ProductDimensions(
            width = 10.0,
            height = 20.0,
            depth = 5.0
        ),
        warrantyInformation = "1 year",
        shippingInformation = "Standard shipping",
        availabilityStatus = "In Stock",
        reviews = listOf(
            ProductReview(
                rating = 5,
                comment = "Great product",
                date = "2026-01-01T10:00:00.000Z",
                reviewerName = "Test Reviewer",
                reviewerEmail = "reviewer@example.com"
            )
        ),
        returnPolicy = "30 days",
        minimumOrderQuantity = 1,
        images = listOf("image1.jpg", "image2.jpg"),
        thumbnail = "thumbnail.jpg"
    )
}
