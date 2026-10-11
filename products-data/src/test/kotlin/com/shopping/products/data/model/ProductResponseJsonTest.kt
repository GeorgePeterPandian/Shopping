package com.shopping.products.data.model

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class ProductResponseJsonTest {

    private lateinit var json: Json

    @Before
    fun setup() {
        json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    private fun loadJson(fileName: String): String = requireNotNull(
        javaClass.classLoader?.getResource("product/$fileName")
    ) {
        "Test resource not found: product/$fileName"
    }.readText()

    @Test
    fun `should parse valid product response`() {
        val response = json.decodeFromString<ProductResponse>(
            loadJson("valid_product_response.json")
        )

        assertEquals(1, response.products.size)

        val product = response.products.first()

        assertEquals(1, product.id)
        assertEquals("iPhone", product.title)
        assertEquals("Apple phone", product.description)
        assertEquals("smartphones", product.category)
        assertEquals(999.99, product.price, 0.001)
        assertEquals(10.5, product.discountPercentage, 0.001)
        assertEquals(4.5, product.rating, 0.001)
        assertEquals(50, product.stock)

        assertEquals(
            listOf("smartphones", "apple"), product.tags
        )

        assertEquals("Apple", product.brand)
        assertEquals("IPHONE-001", product.sku)
        assertEquals(0.17, product.weight, 0.001)

        assertEquals(71.0, product.dimensions.width, 0.001)
        assertEquals(146.0, product.dimensions.height, 0.001)
        assertEquals(8.0, product.dimensions.depth, 0.001)

        assertEquals(
            "1 year manufacturer warranty", product.warrantyInformation
        )

        assertEquals(
            "Ships in 1-2 business days", product.shippingInformation
        )

        assertEquals(
            "In Stock", product.availabilityStatus
        )

        assertEquals(1, product.reviews.size)

        val review = product.reviews.first()

        assertEquals(5, review.rating)
        assertEquals("Excellent phone", review.comment)
        assertEquals("2026-01-01", review.date)
        assertEquals("John Doe", review.reviewerName)
        assertEquals("john@example.com", review.reviewerEmail)

        assertEquals(
            "30 days return policy", product.returnPolicy
        )

        assertEquals(
            1, product.minimumOrderQuantity
        )

        assertEquals(
            listOf("https://example.com/image.jpg"), product.images
        )

        assertEquals(
            "https://example.com/image.jpg", product.thumbnail
        )

        assertEquals(1, response.total)
        assertEquals(0, response.skip)
        assertEquals(20, response.limit)
    }

    @Test
    fun `should ignore unknown fields`() {
        val response = json.decodeFromString<ProductResponse>(
            loadJson("unknown_fields_response.json")
        )

        assertEquals(1, response.products.size)

        val product = response.products.first()

        assertEquals("iPhone", product.title)
        assertEquals("IPHONE-001", product.sku)
        assertEquals(999.99, product.price, 0.001)
    }

    @Test
    fun `should parse multiple products`() {
        val response = json.decodeFromString<ProductResponse>(
            loadJson("multiple_products_response.json")
        )

        assertEquals(2, response.products.size)

        val firstProduct = response.products[0]
        val secondProduct = response.products[1]

        assertEquals("iPhone", firstProduct.title)
        assertEquals("IPHONE-001", firstProduct.sku)

        assertEquals("MacBook", secondProduct.title)
        assertEquals("MACBOOK-001", secondProduct.sku)
    }

    @Test
    fun `should parse empty products list`() {
        val response = json.decodeFromString<ProductResponse>(
            loadJson("empty_products_response.json")
        )

        assertEquals(0, response.products.size)
        assertEquals(0, response.total)
        assertEquals(0, response.skip)
        assertEquals(20, response.limit)
    }

    @Test
    fun `should fail when required response field is missing`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("missing_required_field_response.json")
            )
        }
    }

    @Test
    fun `should fail when json is malformed`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("malformed_json.txt")
            )
        }
    }

    @Test
    fun `should fail when total is invalid string`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("wrong_total_type.json")
            )
        }
    }

    @Test
    fun `should fail when products field is missing`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("missing_products_response.json")
            )
        }
    }

    @Test
    fun `should fail when required product field is missing`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("missing_product_field.json")
            )
        }
    }

    @Test
    fun `should fail when products is null`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("null_products.json")
            )
        }
    }

    @Test
    fun `should fail when required product field is null`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("null_product_title.json")
            )
        }
    }

    @Test
    fun `should fail when products is not an array`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("products_not_array.json")
            )
        }
    }

    @Test
    fun `should fail when product price is invalid`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("invalid_product_price.json")
            )
        }
    }

    @Test
    fun `should fail when root json is array`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("root_array.json")
            )
        }
    }

    @Test
    fun `should fail when root json is null`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("null_root.json")
            )
        }
    }

    @Test
    fun `should fail when json is empty`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("empty_json.txt")
            )
        }
    }

    @Test
    fun `should parse special characters and unicode correctly`() {
        val response = json.decodeFromString<ProductResponse>(
            loadJson("special_characters_response.json")
        )

        val product = response.products.first()

        assertEquals(
            "iPhone \"Pro\" 日本語 📱", product.title
        )

        assertEquals(
            "Phone with émojis 🚀 and special characters: !@#$%^&*()", product.description
        )

        assertEquals(
            "smartphones", product.category
        )

        assertEquals(
            999.99, product.price, 0.001
        )

        assertEquals(
            10.5, product.discountPercentage, 0.001
        )

        assertEquals(
            4.5, product.rating, 0.001
        )

        assertEquals(
            50, product.stock
        )

        assertEquals(
            listOf("smartphones", "apple", "日本語", "📱"), product.tags
        )

        assertEquals(
            "Apple", product.brand
        )

        assertEquals(
            "SPECIAL-001", product.sku
        )

        assertEquals(
            0.17, product.weight, 0.001
        )

        assertEquals(
            71.0, product.dimensions.width, 0.001
        )

        assertEquals(
            146.0, product.dimensions.height, 0.001
        )

        assertEquals(
            8.0, product.dimensions.depth, 0.001
        )

        assertEquals(
            "1 year manufacturer warranty", product.warrantyInformation
        )

        assertEquals(
            "Ships in 1-2 business days", product.shippingInformation
        )

        assertEquals(
            "In Stock", product.availabilityStatus
        )

        assertEquals(
            1, product.reviews.size
        )

        val review = product.reviews.first()

        assertEquals(
            5, review.rating
        )

        assertEquals(
            "Excellent phone 🚀 日本語", review.comment
        )

        assertEquals(
            "2026-01-01", review.date
        )

        assertEquals(
            "José 日本", review.reviewerName
        )

        assertEquals(
            "jose@example.com", review.reviewerEmail
        )

        assertEquals(
            "30 days return policy", product.returnPolicy
        )

        assertEquals(
            1, product.minimumOrderQuantity
        )

        assertEquals(
            listOf("https://example.com/image.jpg"), product.images
        )

        assertEquals(
            "https://example.com/image.jpg", product.thumbnail
        )

        assertEquals(
            1, response.total
        )

        assertEquals(
            0, response.skip
        )

        assertEquals(
            20, response.limit
        )
    }

    @Test
    fun `should parse zero values correctly`() {
        val response = json.decodeFromString<ProductResponse>(
            loadJson("zero_values_response.json")
        )

        assertEquals(0, response.products.size)
        assertEquals(0, response.total)
        assertEquals(0, response.skip)
        assertEquals(0, response.limit)
    }

    @Test
    fun `should parse maximum integer values correctly`() {
        val response = json.decodeFromString<ProductResponse>(
            loadJson("large_integer_values_response.json")
        )

        assertEquals(Int.MAX_VALUE, response.total)
        assertEquals(Int.MAX_VALUE, response.skip)
        assertEquals(Int.MAX_VALUE, response.limit)
    }

    @Test
    fun `should fail when integer exceeds maximum value`() {
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<ProductResponse>(
                loadJson("integer_overflow_response.json")
            )
        }
    }
}