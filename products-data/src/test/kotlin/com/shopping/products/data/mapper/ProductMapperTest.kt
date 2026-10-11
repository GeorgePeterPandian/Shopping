package com.shopping.products.data.mapper

import com.shopping.products.data.model.ProductDimensionsResponse
import com.shopping.products.data.model.ProductMetaResponse
import com.shopping.products.data.model.ProductResponseItem
import com.shopping.products.data.model.ProductReviewResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `should map product response item to domain product`() {
        val response = ProductResponseItem(
            id = 1,
            title = "iPhone",
            description = "Apple smartphone",
            category = "smartphones",
            price = 999.99,
            discountPercentage = 10.5,
            rating = 4.7,
            stock = 50,
            tags = listOf("smartphones", "apple"),
            brand = "Apple",
            sku = "IPHONE-001",
            weight = 200.0,
            dimensions = ProductDimensionsResponse(
                width = 7.1, height = 14.7, depth = 0.8
            ),
            warrantyInformation = "1 year warranty",
            shippingInformation = "Ships in 2 days",
            availabilityStatus = "In Stock",
            reviews = listOf(
                ProductReviewResponse(
                    rating = 5,
                    comment = "Excellent phone",
                    date = "2026-01-01T00:00:00.000Z",
                    reviewerName = "John Doe",
                    reviewerEmail = "john@example.com"
                )
            ),
            returnPolicy = "30 days return",
            minimumOrderQuantity = 1,
            meta = ProductMetaResponse(
                createdAt = "2026-01-01T00:00:00.000Z",
                updatedAt = "2026-01-02T00:00:00.000Z",
                barcode = "123456789",
                qrCode = "https://example.com/qr"
            ),
            images = listOf(
                "https://example.com/iphone-1.jpg", "https://example.com/iphone-2.jpg"
            ),
            thumbnail = "iphone.jpg"
        )

        val result = response.toDomain()

        assertEquals(1, result.id)
        assertEquals("iPhone", result.title)
        assertEquals("Apple smartphone", result.description)
        assertEquals("smartphones", result.category)
        assertEquals(999.99, result.price, 0.0)
        assertEquals(10.5, result.discountPercentage, 0.0)
        assertEquals(4.7, result.rating, 0.0)
        assertEquals(50, result.stock)

        assertEquals(
            listOf("smartphones", "apple"), result.tags
        )
        assertEquals("Apple", result.brand)
        assertEquals("IPHONE-001", result.sku)
        assertEquals(200.0, result.weight, 0.0)

        assertEquals(7.1, result.dimensions.width, 0.0)
        assertEquals(14.7, result.dimensions.height, 0.0)
        assertEquals(0.8, result.dimensions.depth, 0.0)

        assertEquals(
            "1 year warranty", result.warrantyInformation
        )
        assertEquals(
            "Ships in 2 days", result.shippingInformation
        )
        assertEquals(
            "In Stock", result.availabilityStatus
        )

        assertEquals(1, result.reviews.size)

        val review = result.reviews.first()
        assertEquals(5, review.rating)
        assertEquals("Excellent phone", review.comment)
        assertEquals(
            "2026-01-01T00:00:00.000Z", review.date
        )
        assertEquals("John Doe", review.reviewerName)
        assertEquals(
            "john@example.com", review.reviewerEmail
        )

        assertEquals(
            "30 days return", result.returnPolicy
        )
        assertEquals(1, result.minimumOrderQuantity)

        assertEquals(
            listOf(
                "https://example.com/iphone-1.jpg", "https://example.com/iphone-2.jpg"
            ), result.images
        )

        assertEquals("iphone.jpg", result.thumbnail)
    }

    @Test
    fun `should map zero and empty values correctly`() {
        val response = ProductResponseItem(
            id = 0,
            title = "",
            description = "",
            category = "",
            price = 0.0,
            discountPercentage = 0.0,
            rating = 0.0,
            stock = 0,
            tags = emptyList(),
            brand = null,
            sku = "",
            weight = 0.0,
            dimensions = ProductDimensionsResponse(
                width = 0.0, height = 0.0, depth = 0.0
            ),
            warrantyInformation = "",
            shippingInformation = "",
            availabilityStatus = "",
            reviews = emptyList(),
            returnPolicy = "",
            minimumOrderQuantity = 0,
            meta = ProductMetaResponse(
                createdAt = "", updatedAt = "", barcode = "", qrCode = ""
            ),
            images = emptyList(),
            thumbnail = ""
        )

        val result = response.toDomain()

        assertEquals(0, result.id)
        assertEquals("", result.title)
        assertEquals("", result.description)
        assertEquals("", result.category)
        assertEquals(0.0, result.price, 0.0)
        assertEquals(0.0, result.discountPercentage, 0.0)
        assertEquals(0.0, result.rating, 0.0)
        assertEquals(0, result.stock)
        assertEquals(emptyList<String>(), result.tags)
        assertEquals(null, result.brand)
        assertEquals("", result.sku)
        assertEquals(0.0, result.weight, 0.0)
        assertEquals(0.0, result.dimensions.width, 0.0)
        assertEquals(0.0, result.dimensions.height, 0.0)
        assertEquals(0.0, result.dimensions.depth, 0.0)
        assertEquals("", result.warrantyInformation)
        assertEquals("", result.shippingInformation)
        assertEquals("", result.availabilityStatus)
        assertEquals(emptyList<String>(), result.images)
        assertEquals("", result.thumbnail)
    }

    @Test
    fun `should preserve special characters and unicode values`() {
        val response = ProductResponseItem(
            id = 1,
            title = "iPhone Pro 📱",
            description = "Apple phone — 100% original & premium",
            category = "smartphones & accessories",
            price = 999.99,
            discountPercentage = 10.5,
            rating = 4.7,
            stock = 50,
            tags = listOf("phones", "premium 📱"),
            brand = "Apple & Co.",
            sku = "IPHONE-©-001",
            weight = 1.5,
            dimensions = ProductDimensionsResponse(
                width = 7.1, height = 14.7, depth = 0.8
            ),
            warrantyInformation = "1 year — full warranty",
            shippingInformation = "Ships worldwide 🌍",
            availabilityStatus = "In Stock ✓",
            reviews = listOf(
                ProductReviewResponse(
                    rating = 5,
                    comment = "Excellent 📱 — highly recommended!",
                    date = "2026-01-01T00:00:00.000Z",
                    reviewerName = "José Müller",
                    reviewerEmail = "jose@example.com"
                )
            ),
            returnPolicy = "30 days — no questions asked",
            minimumOrderQuantity = 1,
            meta = ProductMetaResponse(
                createdAt = "2026-01-01T00:00:00.000Z",
                updatedAt = "2026-01-02T00:00:00.000Z",
                barcode = "123456789",
                qrCode = "https://example.com/qr?id=1&lang=en"
            ),
            images = listOf(
                "https://example.com/image?id=1&size=large"
            ),
            thumbnail = "https://example.com/image?id=1&size=large"
        )

        val result = response.toDomain()

        assertEquals("iPhone Pro 📱", result.title)
        assertEquals(
            "Apple phone — 100% original & premium", result.description
        )
        assertEquals(
            "smartphones & accessories", result.category
        )
        assertEquals(
            listOf("phones", "premium 📱"), result.tags
        )
        assertEquals("Apple & Co.", result.brand)
        assertEquals(
            "Ships worldwide 🌍", result.shippingInformation
        )
        assertEquals(
            "José Müller", result.reviews.first().reviewerName
        )
        assertEquals(
            "Excellent 📱 — highly recommended!", result.reviews.first().comment
        )
        assertEquals(
            "https://example.com/image?id=1&size=large", result.thumbnail
        )
    }

    @Test
    fun `should preserve maximum numeric values`() {
        val response = ProductResponseItem(
            id = Int.MAX_VALUE,
            title = "Maximum values",
            description = "Boundary test",
            category = "test",
            price = Double.MAX_VALUE,
            discountPercentage = Double.MAX_VALUE,
            rating = Double.MAX_VALUE,
            stock = Int.MAX_VALUE,
            tags = listOf("maximum"),
            brand = "Maximum Brand",
            sku = "MAX-001",
            weight = Double.MAX_VALUE,
            dimensions = ProductDimensionsResponse(
                width = Double.MAX_VALUE, height = Double.MAX_VALUE, depth = Double.MAX_VALUE
            ),
            warrantyInformation = "Maximum warranty",
            shippingInformation = "Maximum shipping",
            availabilityStatus = "Maximum availability",
            reviews = listOf(
                ProductReviewResponse(
                    rating = Int.MAX_VALUE,
                    comment = "Maximum",
                    date = "9999-12-31T23:59:59.999Z",
                    reviewerName = "Maximum",
                    reviewerEmail = "maximum@example.com"
                )
            ),
            returnPolicy = "Maximum return",
            minimumOrderQuantity = Int.MAX_VALUE,
            meta = ProductMetaResponse(
                createdAt = "9999-12-31T23:59:59.999Z",
                updatedAt = "9999-12-31T23:59:59.999Z",
                barcode = "MAX",
                qrCode = "MAX"
            ),
            images = listOf("maximum.jpg"),
            thumbnail = "maximum.jpg"
        )

        val result = response.toDomain()

        assertEquals(Int.MAX_VALUE, result.id)
        assertEquals(Double.MAX_VALUE, result.price, 0.0)
        assertEquals(Double.MAX_VALUE, result.discountPercentage, 0.0)
        assertEquals(Double.MAX_VALUE, result.rating, 0.0)
        assertEquals(Int.MAX_VALUE, result.stock)
        assertEquals(Double.MAX_VALUE, result.weight, 0.0)
        assertEquals(
            Double.MAX_VALUE, result.dimensions.width, 0.0
        )
        assertEquals(
            Double.MAX_VALUE, result.dimensions.height, 0.0
        )
        assertEquals(
            Double.MAX_VALUE, result.dimensions.depth, 0.0
        )
        assertEquals(
            Int.MAX_VALUE, result.minimumOrderQuantity
        )
        assertEquals(
            Int.MAX_VALUE, result.reviews.first().rating
        )
    }

    @Test
    fun `should preserve negative numeric values`() {
        val response = ProductResponseItem(
            id = -1,
            title = "Negative values",
            description = "Boundary test",
            category = "test",
            price = -999.99,
            discountPercentage = -10.5,
            rating = -4.7,
            stock = -50,
            tags = listOf("negative"),
            brand = "Negative Brand",
            sku = "NEG-001",
            weight = -1.5,
            dimensions = ProductDimensionsResponse(
                width = -1.0, height = -2.0, depth = -3.0
            ),
            warrantyInformation = "Negative warranty",
            shippingInformation = "Negative shipping",
            availabilityStatus = "Negative",
            reviews = listOf(
                ProductReviewResponse(
                    rating = -5,
                    comment = "Negative",
                    date = "2026-01-01T00:00:00.000Z",
                    reviewerName = "Negative",
                    reviewerEmail = "negative@example.com"
                )
            ),
            returnPolicy = "Negative return",
            minimumOrderQuantity = -1,
            meta = ProductMetaResponse(
                createdAt = "", updatedAt = "", barcode = "", qrCode = ""
            ),
            images = listOf("image.jpg"),
            thumbnail = "image.jpg"
        )

        val result = response.toDomain()

        assertEquals(-1, result.id)
        assertEquals("Negative values", result.title)
        assertEquals("Boundary test", result.description)
        assertEquals(-999.99, result.price, 0.0)
        assertEquals(-10.5, result.discountPercentage, 0.0)
        assertEquals(-4.7, result.rating, 0.0)
        assertEquals(-50, result.stock)
        assertEquals(-1.5, result.weight, 0.0)
        assertEquals(-1.0, result.dimensions.width, 0.0)
        assertEquals(-2.0, result.dimensions.height, 0.0)
        assertEquals(-3.0, result.dimensions.depth, 0.0)
        assertEquals(-5, result.reviews.first().rating)
        assertEquals(-1, result.minimumOrderQuantity)
        assertEquals("image.jpg", result.thumbnail)
    }
}