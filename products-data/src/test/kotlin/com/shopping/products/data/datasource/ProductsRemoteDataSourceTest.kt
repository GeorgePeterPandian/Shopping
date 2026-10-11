package com.shopping.products.data.datasource

import com.shopping.products.data.model.ProductDimensionsResponse
import com.shopping.products.data.model.ProductMetaResponse
import com.shopping.products.data.model.ProductResponse
import com.shopping.products.data.model.ProductResponseItem
import com.shopping.products.data.model.ProductReviewResponse
import com.shopping.products.data.service.ProductsApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ProductsRemoteDataSourceTest {

    private lateinit var api: ProductsApi
    private lateinit var dataSource: ProductsRemoteDataSourceImpl

    @Before
    fun setup() {
        api = mockk()
        dataSource = ProductsRemoteDataSourceImpl(api)
    }

    @Test
    fun `should return products from api`() = runTest {
        val expectedResponse = ProductResponse(
            products = listOf(
                ProductResponseItem(
                    id = 1,
                    title = "iPhone",
                    description = "Apple phone",
                    category = "smartphones",
                    price = 999.99,
                    discountPercentage = 10.5,
                    rating = 4.5,
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
                    images = listOf("image.jpg"),
                    thumbnail = "image.jpg"
                )
            ), total = 1, skip = 0, limit = 20
        )

        coEvery {
            api.getProducts(limit = 20, skip = 0)
        } returns expectedResponse

        val result = dataSource.getProducts(limit = 20, skip = 0)

        assertEquals(expectedResponse, result)

        coVerify(exactly = 1) {
            api.getProducts(limit = 20, skip = 0)
        }
    }

    @Test
    fun `should return empty response from api`() = runTest {
        val expectedResponse = ProductResponse(
            products = emptyList(), total = 0, skip = 0, limit = 20
        )

        coEvery {
            api.getProducts(limit = 20, skip = 0)
        } returns expectedResponse

        val result = dataSource.getProducts(limit = 20, skip = 0)

        assertEquals(expectedResponse, result)
        assertEquals(0, result.products.size)
        assertEquals(0, result.total)
        assertEquals(0, result.skip)
        assertEquals(20, result.limit)

        coVerify(exactly = 1) {
            api.getProducts(limit = 20, skip = 0)
        }
    }

    @Test
    fun `should forward pagination values to api`() = runTest {
        val expectedResponse = ProductResponse(
            products = emptyList(), total = 100, skip = 40, limit = 10
        )

        coEvery {
            api.getProducts(limit = 10, skip = 40)
        } returns expectedResponse

        val result = dataSource.getProducts(limit = 10, skip = 40)

        assertEquals(expectedResponse, result)
        assertEquals(100, result.total)
        assertEquals(40, result.skip)
        assertEquals(10, result.limit)

        coVerify(exactly = 1) {
            api.getProducts(limit = 10, skip = 40)
        }
    }

    @Test
    fun `should propagate exception from api`() = runTest {
        val exception = RuntimeException("Network error")

        coEvery {
            api.getProducts(limit = 20, skip = 0)
        } throws exception

        val thrownException = try {
            dataSource.getProducts(limit = 20, skip = 0)
            null
        } catch (e: Exception) {
            e
        }

        assertEquals(exception, thrownException)

        coVerify(exactly = 1) {
            api.getProducts(limit = 20, skip = 0)
        }
    }

    @Test
    fun `should propagate HttpException from api`() = runTest {
        val exception = HttpException(
            Response.error<Any>(
                500,
                """{"message":"Internal server error"}""".toResponseBody("application/json".toMediaType())
            )
        )

        coEvery {
            api.getProducts(limit = 20, skip = 0)
        } throws exception

        val thrownException = try {
            dataSource.getProducts(limit = 20, skip = 0)
            null
        } catch (e: HttpException) {
            e
        }

        assertNotNull(thrownException)
        assertEquals(500, thrownException?.code())

        coVerify(exactly = 1) {
            api.getProducts(limit = 20, skip = 0)
        }
    }

    @Test
    fun `should return search products from api`() = runTest {
        val response = ProductResponse(
            products = listOf(
                ProductResponseItem(
                    id = 1,
                    title = "iPhone",
                    description = "Apple phone",
                    category = "smartphones",
                    price = 999.99,
                    discountPercentage = 10.0,
                    rating = 4.5,
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
                    reviews = emptyList(),
                    returnPolicy = "30 days return",
                    minimumOrderQuantity = 1,
                    meta = ProductMetaResponse(
                        createdAt = "2026-01-01T00:00:00.000Z",
                        updatedAt = "2026-01-02T00:00:00.000Z",
                        barcode = "123456789",
                        qrCode = "https://example.com/qr"
                    ),
                    images = listOf("image.jpg"),
                    thumbnail = "image.jpg"
                )
            ), total = 1, skip = 0, limit = 20
        )

        coEvery {
            api.searchProducts(query = "phone", limit = 20, skip = 0)
        } returns response

        val result = dataSource.searchProducts(query = "phone", limit = 20, skip = 0)

        assertEquals(response, result)

        coVerify(exactly = 1) {
            api.searchProducts(query = "phone", limit = 20, skip = 0)
        }
    }

    @Test
    fun `getProduct should return product from api`() = runTest {
        val expected = ProductResponseItem(
            id = 1,
            title = "Essence Mascara Lash Princess",
            description = "The Essence Mascara Lash Princess is a popular mascara.",
            category = "beauty",
            price = 9.99,
            discountPercentage = 7.17,
            rating = 4.94,
            stock = 5,
            tags = listOf("beauty", "mascara"),
            brand = "Essence",
            sku = "BEA-ESS-MAS-001",
            weight = 2.0,
            dimensions = ProductDimensionsResponse(
                width = 2.0, height = 10.0, depth = 2.0
            ),
            warrantyInformation = "No warranty",
            shippingInformation = "Ships in 3-5 days",
            availabilityStatus = "In Stock",
            reviews = listOf(
                ProductReviewResponse(
                    rating = 5,
                    comment = "Very good mascara",
                    date = "2026-01-01T00:00:00.000Z",
                    reviewerName = "Alice",
                    reviewerEmail = "alice@example.com"
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
                "https://cdn.dummyjson.com/product-images/1/1.jpg"
            ),
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        )

        coEvery {
            api.getProduct(1)
        } returns expected

        val result = dataSource.getProduct(1)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            api.getProduct(1)
        }
    }
}