package com.shopping.products.data.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit

class ProductsApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: ProductsApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        val retrofit = Retrofit.Builder().baseUrl(mockWebServer.url("/")).addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            ).build()

        api = retrofit.create(ProductsApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should get products with limit and skip parameters`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    {
                      "products": [
                        {
                          "id": 1,
                          "title": "iPhone",
                          "description": "Apple phone",
                          "category": "smartphones",
                          "price": 999.99,
                          "discountPercentage": 10.5,
                          "rating": 4.5,
                          "stock": 50,
                          "tags": ["smartphones", "apple"],
                          "brand": "Apple",
                          "sku": "IPHONE-001",
                          "weight": 200.0,
                          "dimensions": {
                            "width": 7.1,
                            "height": 14.7,
                            "depth": 0.8
                          },
                          "warrantyInformation": "1 year warranty",
                          "shippingInformation": "Ships in 2 days",
                          "availabilityStatus": "In Stock",
                          "reviews": [
                            {
                              "rating": 5,
                              "comment": "Excellent phone",
                              "date": "2026-01-01T00:00:00.000Z",
                              "reviewerName": "John Doe",
                              "reviewerEmail": "john@example.com"
                            }
                          ],
                          "returnPolicy": "30 days return",
                          "minimumOrderQuantity": 1,
                          "meta": {
                            "createdAt": "2026-01-01T00:00:00.000Z",
                            "updatedAt": "2026-01-02T00:00:00.000Z",
                            "barcode": "123456789",
                            "qrCode": "https://example.com/qr"
                          },
                          "images": ["image.jpg"],
                          "thumbnail": "image.jpg"
                        }
                      ],
                      "total": 1,
                      "skip": 20,
                      "limit": 20
                    }
                    """.trimIndent()
                )
        )

        val response = api.getProducts(
            limit = 20, skip = 20
        )

        assertEquals(1, response.products.size)

        val product = response.products.first()

        assertEquals("iPhone", product.title)
        assertEquals("smartphones", product.category)
        assertEquals("Apple", product.brand)
        assertEquals(listOf("smartphones", "apple"), product.tags)
        assertEquals(7.1, product.dimensions.width, 0.0)
        assertEquals(14.7, product.dimensions.height, 0.0)
        assertEquals(0.8, product.dimensions.depth, 0.0)
        assertEquals(1, product.reviews.size)
        assertEquals("John Doe", product.reviews.first().reviewerName)
        assertEquals(1, product.minimumOrderQuantity)
        assertEquals(listOf("image.jpg"), product.images)

        assertEquals(1, response.total)
        assertEquals(20, response.skip)
        assertEquals(20, response.limit)

        val request = mockWebServer.takeRequest()

        assertEquals("GET", request.method)
        assertEquals(
            "/products?limit=20&skip=20", request.path
        )
    }

    @Test
    fun `should parse multiple products from response`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    {
                      "products": [
                        {
                          "id": 1,
                          "title": "iPhone",
                          "description": "Phone",
                          "category": "smartphones",
                          "price": 999.0,
                          "discountPercentage": 10.0,
                          "rating": 4.5,
                          "stock": 10,
                          "tags": ["smartphones"],
                          "brand": "Apple",
                          "sku": "IPHONE-001",
                          "weight": 200.0,
                          "dimensions": {
                            "width": 7.0,
                            "height": 14.0,
                            "depth": 0.8
                          },
                          "warrantyInformation": "1 year",
                          "shippingInformation": "Ships in 2 days",
                          "availabilityStatus": "In Stock",
                          "reviews": [],
                          "returnPolicy": "30 days",
                          "minimumOrderQuantity": 1,
                          "meta": {
                            "createdAt": "2026-01-01T00:00:00.000Z",
                            "updatedAt": "2026-01-02T00:00:00.000Z",
                            "barcode": "111",
                            "qrCode": "qr1"
                          },
                          "images": ["image1.jpg"],
                          "thumbnail": "image1.jpg"
                        },
                        {
                          "id": 2,
                          "title": "MacBook",
                          "description": "Laptop",
                          "category": "laptops",
                          "price": 1999.0,
                          "discountPercentage": 5.0,
                          "rating": 4.8,
                          "stock": 5,
                          "tags": ["laptops"],
                          "brand": "Apple",
                          "sku": "MACBOOK-001",
                          "weight": 1.5,
                          "dimensions": {
                            "width": 30.0,
                            "height": 20.0,
                            "depth": 2.0
                          },
                          "warrantyInformation": "1 year",
                          "shippingInformation": "Ships in 3 days",
                          "availabilityStatus": "In Stock",
                          "reviews": [],
                          "returnPolicy": "30 days",
                          "minimumOrderQuantity": 1,
                          "meta": {
                            "createdAt": "2026-01-01T00:00:00.000Z",
                            "updatedAt": "2026-01-02T00:00:00.000Z",
                            "barcode": "222",
                            "qrCode": "qr2"
                          },
                          "images": ["image2.jpg"],
                          "thumbnail": "image2.jpg"
                        }
                      ],
                      "total": 2,
                      "skip": 0,
                      "limit": 20
                    }
                    """.trimIndent()
                )
        )

        val response = api.getProducts(
            limit = 20, skip = 0
        )

        assertEquals(2, response.products.size)
        assertEquals("iPhone", response.products[0].title)
        assertEquals("MacBook", response.products[1].title)
        assertEquals("Apple", response.products[0].brand)
        assertEquals("Apple", response.products[1].brand)
        assertEquals(2, response.total)
    }

    @Test
    fun `should handle empty products response`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    {
                      "products": [],
                      "total": 0,
                      "skip": 0,
                      "limit": 20
                    }
                    """.trimIndent()
                )
        )

        val response = api.getProducts(
            limit = 20, skip = 0
        )

        assertEquals(0, response.products.size)
        assertEquals(0, response.total)
        assertEquals(0, response.skip)
        assertEquals(20, response.limit)
    }

    @Test
    fun `should throw HttpException for bad request`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(400).setBody("""{"message":"Bad request"}""")
        )

        val exception = try {
            api.getProducts(
                limit = 20, skip = 0
            )
            throw AssertionError("Expected HttpException")
        } catch (exception: HttpException) {
            exception
        }

        assertEquals(400, exception.code())
    }

    @Test
    fun `should throw HttpException for not found`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(404).setBody("""{"message":"Products not found"}""")
        )

        val exception = try {
            api.getProducts(
                limit = 20, skip = 0
            )
            throw AssertionError("Expected HttpException")
        } catch (exception: HttpException) {
            exception
        }

        assertEquals(404, exception.code())
    }

    @Test
    fun `should throw HttpException for internal server error`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(500).setBody("""{"message":"Internal server error"}""")
        )

        val exception = try {
            api.getProducts(
                limit = 20, skip = 0
            )
            throw AssertionError("Expected HttpException")
        } catch (exception: HttpException) {
            exception
        }

        assertEquals(500, exception.code())
    }

    @Test
    fun `should throw HttpException for service unavailable`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(503).setBody("""{"message":"Service unavailable"}""")
        )

        val exception = try {
            api.getProducts(
                limit = 20, skip = 0
            )
            throw AssertionError("Expected HttpException")
        } catch (exception: HttpException) {
            exception
        }

        assertEquals(503, exception.code())
    }

    @Test
    fun `should throw exception for malformed json response`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody("""{"products": [invalid json}""")
        )

        try {
            api.getProducts(
                limit = 20, skip = 0
            )

            fail("Expected exception for malformed JSON")
        } catch (exception: Exception) {
            assertNotNull(exception)
        }
    }

    @Test
    fun `should throw exception when required response field is missing`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                {
                  "products": [],
                  "skip": 0,
                  "limit": 20
                }
                """.trimIndent()
                )
        )

        try {
            api.getProducts(
                limit = 20, skip = 0
            )

            fail("Expected exception when required field is missing")
        } catch (exception: Exception) {
            assertNotNull(exception)
        }
    }

    @Test
    fun `should ignore unknown fields in response`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    {
                      "products": [
                        {
                          "id": 1,
                          "title": "iPhone",
                          "description": "Apple phone",
                          "category": "smartphones",
                          "price": 999.99,
                          "discountPercentage": 10.5,
                          "rating": 4.5,
                          "stock": 50,
                          "tags": ["smartphones"],
                          "brand": "Apple",
                          "sku": "IPHONE-001",
                          "weight": 200.0,
                          "dimensions": {
                            "width": 7.1,
                            "height": 14.7,
                            "depth": 0.8
                          },
                          "warrantyInformation": "1 year",
                          "shippingInformation": "Ships in 2 days",
                          "availabilityStatus": "In Stock",
                          "reviews": [],
                          "returnPolicy": "30 days",
                          "minimumOrderQuantity": 1,
                          "meta": {
                            "createdAt": "2026-01-01T00:00:00.000Z",
                            "updatedAt": "2026-01-02T00:00:00.000Z",
                            "barcode": "123",
                            "qrCode": "qr"
                          },
                          "images": ["image.jpg"],
                          "thumbnail": "image.jpg",
                          "unknownField": "should be ignored"
                        }
                      ],
                      "total": 1,
                      "skip": 0,
                      "limit": 20,
                      "unknownResponseField": true
                    }
                    """.trimIndent()
                )
        )

        val response = api.getProducts(
            limit = 20, skip = 0
        )

        assertEquals(1, response.products.size)
        assertEquals(1, response.products.first().id)
        assertEquals("iPhone", response.products.first().title)
        assertEquals("Apple", response.products.first().brand)
    }

    @Test
    fun `should throw exception for empty response body`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody("")
        )

        val exception = try {
            api.getProducts(
                limit = 20, skip = 0
            )
            null
        } catch (exception: Exception) {
            exception
        }

        assertNotNull(exception)
    }

    @Test
    fun `should throw exception when product price has invalid type`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    {
                      "products": [
                        {
                          "id": 1,
                          "title": "iPhone",
                          "description": "Apple phone",
                          "category": "smartphones",
                          "price": "not-a-number",
                          "discountPercentage": 10.5,
                          "rating": 4.5,
                          "stock": 50,
                          "tags": [],
                          "brand": "Apple",
                          "sku": "IPHONE-001",
                          "weight": 200.0,
                          "dimensions": {
                            "width": 7.1,
                            "height": 14.7,
                            "depth": 0.8
                          },
                          "warrantyInformation": "1 year",
                          "shippingInformation": "Ships in 2 days",
                          "availabilityStatus": "In Stock",
                          "reviews": [],
                          "returnPolicy": "30 days",
                          "minimumOrderQuantity": 1,
                          "meta": {
                            "createdAt": "2026-01-01T00:00:00.000Z",
                            "updatedAt": "2026-01-02T00:00:00.000Z",
                            "barcode": "123",
                            "qrCode": "qr"
                          },
                          "images": ["image.jpg"],
                          "thumbnail": "image.jpg"
                        }
                      ],
                      "total": 1,
                      "skip": 0,
                      "limit": 20
                    }
                    """.trimIndent()
                )
        )

        val exception = try {
            api.getProducts(
                limit = 20, skip = 0
            )
            null
        } catch (exception: Exception) {
            exception
        }

        assertNotNull(exception)
    }

    @Test
    fun `should throw exception when network request fails`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        )

        val exception = try {
            api.getProducts(
                limit = 20, skip = 0
            )
            null
        } catch (exception: Exception) {
            exception
        }

        assertNotNull(exception)
    }

    @Test
    fun `should send correct pagination query parameters`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    {
                      "products": [],
                      "total": 100,
                      "skip": 40,
                      "limit": 10
                    }
                    """.trimIndent()
                )
        )

        api.getProducts(
            limit = 10, skip = 40
        )

        val request = mockWebServer.takeRequest()

        assertEquals("GET", request.method)
        assertEquals(
            "/products?limit=10&skip=40", request.path
        )
    }

    @Test
    fun `should send zero pagination values`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    {
                      "products": [],
                      "total": 0,
                      "skip": 0,
                      "limit": 0
                    }
                    """.trimIndent()
                )
        )

        val response = api.getProducts(
            limit = 0, skip = 0
        )

        assertEquals(0, response.products.size)
        assertEquals(0, response.total)
        assertEquals(0, response.skip)
        assertEquals(0, response.limit)

        val request = mockWebServer.takeRequest()

        assertEquals("GET", request.method)
        assertEquals(
            "/products?limit=0&skip=0", request.path
        )
    }

    @Test
    fun `should send large pagination values`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    {
                      "products": [],
                      "total": 2147483647,
                      "skip": 2147483647,
                      "limit": 2147483647
                    }
                    """.trimIndent()
                )
        )

        val response = api.getProducts(
            limit = Int.MAX_VALUE, skip = Int.MAX_VALUE
        )

        assertEquals(Int.MAX_VALUE, response.total)
        assertEquals(Int.MAX_VALUE, response.skip)
        assertEquals(Int.MAX_VALUE, response.limit)

        val request = mockWebServer.takeRequest()

        assertEquals("GET", request.method)
        assertEquals(
            "/products?limit=${Int.MAX_VALUE}&skip=${Int.MAX_VALUE}", request.path
        )
    }

    @Test
    fun `should send negative pagination values`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    {
                      "products": [],
                      "total": 0,
                      "skip": -10,
                      "limit": -20
                    }
                    """.trimIndent()
                )
        )

        val response = api.getProducts(
            limit = -20, skip = -10
        )

        assertEquals(0, response.products.size)
        assertEquals(-10, response.skip)
        assertEquals(-20, response.limit)

        val request = mockWebServer.takeRequest()

        assertEquals("GET", request.method)
        assertEquals(
            "/products?limit=-20&skip=-10", request.path
        )
    }

    @Test
    fun `getProduct should request product by id`() = runTest {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
                .setBody(
                    """
                    {
                      "id": 1,
                      "title": "Essence Mascara Lash Princess",
                      "description": "The Essence Mascara Lash Princess is a popular mascara.",
                      "category": "beauty",
                      "price": 9.99,
                      "discountPercentage": 7.17,
                      "rating": 4.94,
                      "stock": 5,
                      "tags": ["beauty", "mascara"],
                      "brand": "Essence",
                      "sku": "BEA-ESS-MAS-001",
                      "weight": 2.0,
                      "dimensions": {
                        "width": 2.0,
                        "height": 10.0,
                        "depth": 2.0
                      },
                      "warrantyInformation": "No warranty",
                      "shippingInformation": "Ships in 3-5 days",
                      "availabilityStatus": "In Stock",
                      "reviews": [
                        {
                          "rating": 5,
                          "comment": "Very good mascara",
                          "date": "2026-01-01T00:00:00.000Z",
                          "reviewerName": "Alice",
                          "reviewerEmail": "alice@example.com"
                        }
                      ],
                      "returnPolicy": "30 days return",
                      "minimumOrderQuantity": 1,
                      "meta": {
                        "createdAt": "2026-01-01T00:00:00.000Z",
                        "updatedAt": "2026-01-02T00:00:00.000Z",
                        "barcode": "123456789",
                        "qrCode": "https://example.com/qr"
                      },
                      "images": [
                        "https://cdn.dummyjson.com/product-images/1/1.jpg"
                      ],
                      "thumbnail": "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
                    }
                    """.trimIndent()
                )
        )

        val product = api.getProduct(1)

        assertEquals(1, product.id)
        assertEquals(
            "Essence Mascara Lash Princess", product.title
        )
        assertEquals(
            "The Essence Mascara Lash Princess is a popular mascara.", product.description
        )
        assertEquals("beauty", product.category)
        assertEquals(9.99, product.price, 0.001)
        assertEquals(7.17, product.discountPercentage, 0.001)
        assertEquals(4.94, product.rating, 0.001)
        assertEquals(5, product.stock)
        assertEquals(listOf("beauty", "mascara"), product.tags)
        assertEquals("Essence", product.brand)
        assertEquals("BEA-ESS-MAS-001", product.sku)
        assertEquals(2.0, product.weight, 0.0)
        assertEquals(2.0, product.dimensions.width, 0.0)
        assertEquals(10.0, product.dimensions.height, 0.0)
        assertEquals(2.0, product.dimensions.depth, 0.0)
        assertEquals("No warranty", product.warrantyInformation)
        assertEquals(
            "Ships in 3-5 days", product.shippingInformation
        )
        assertEquals(
            "In Stock", product.availabilityStatus
        )
        assertEquals(1, product.reviews.size)
        assertEquals(
            "Very good mascara", product.reviews.first().comment
        )
        assertEquals(
            "Alice", product.reviews.first().reviewerName
        )
        assertEquals(
            "30 days return", product.returnPolicy
        )
        assertEquals(1, product.minimumOrderQuantity)
        assertEquals(
            listOf(
                "https://cdn.dummyjson.com/product-images/1/1.jpg"
            ), product.images
        )
        assertEquals(
            "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg", product.thumbnail
        )

        val request = mockWebServer.takeRequest()

        assertEquals(
            "/products/1", request.path
        )
        assertEquals("GET", request.method)
    }
}