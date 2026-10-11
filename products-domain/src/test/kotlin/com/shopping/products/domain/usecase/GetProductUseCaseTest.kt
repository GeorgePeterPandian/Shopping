package com.shopping.products.domain.usecase

import com.shopping.products.domain.model.Product
import com.shopping.products.domain.model.ProductDimensions
import com.shopping.products.domain.model.ProductReview
import com.shopping.products.domain.repository.ProductsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProductUseCaseTest {

    private lateinit var repository: ProductsRepository
    private lateinit var useCase: GetProductUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetProductUseCase(repository)
    }

    @Test
    fun `invoke should return product from repository`() = runTest {
        val expected = Product(
            id = 1,
            title = "Essence Mascara Lash Princess",
            description = "The Essence Mascara Lash Princess is a popular mascara.",
            category = "beauty",
            price = 9.99,
            discountPercentage = 7.17,
            rating = 4.94,
            stock = 5,
            tags = listOf(
                "beauty", "mascara"
            ),
            brand = "Essence",
            sku = "ESS-001",
            weight = 0.05,
            dimensions = ProductDimensions(
                width = 2.0, height = 10.0, depth = 2.0
            ),
            warrantyInformation = "1 year warranty",
            shippingInformation = "Ships in 1-2 business days",
            availabilityStatus = "In Stock",
            reviews = listOf(
                ProductReview(
                    rating = 5,
                    comment = "Great mascara",
                    date = "2026-01-01",
                    reviewerName = "John Doe",
                    reviewerEmail = "john@example.com"
                )
            ),
            returnPolicy = "30 days return policy",
            minimumOrderQuantity = 1,
            images = listOf(
                "https://cdn.dummyjson.com/product-images/1/1.jpg"
            ),
            thumbnail = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
        )

        coEvery {
            repository.getProduct(1)
        } returns expected

        val result = useCase(1)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            repository.getProduct(1)
        }
    }
}