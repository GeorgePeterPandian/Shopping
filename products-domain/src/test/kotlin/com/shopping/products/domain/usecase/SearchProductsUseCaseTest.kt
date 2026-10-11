package com.shopping.products.domain.usecase

import androidx.paging.PagingData
import com.shopping.products.domain.model.Product
import com.shopping.products.domain.model.ProductDimensions
import com.shopping.products.domain.model.ProductReview
import com.shopping.products.domain.repository.ProductsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SearchProductsUseCaseTest {

    private val repository: ProductsRepository = mockk()
    private val useCase = SearchProductsUseCase(repository)

    @Test
    fun `should search products`() = runTest {
        every {
            repository.searchProducts("phone")
        } returns flowOf(
            PagingData.from(listOf(createProduct()))
        )

        val result = useCase(query = "phone").first()

        assertNotNull(result)

        verify(exactly = 1) {
            repository.searchProducts("phone")
        }
    }

    @Test
    fun `should return empty PagingData when no products match`() = runTest {
        every {
            repository.searchProducts("unknown")
        } returns flowOf(PagingData.empty<Product>())

        val result = useCase(query = "unknown").first()

        assertNotNull(result)

        verify(exactly = 1) {
            repository.searchProducts("unknown")
        }
    }

    @Test
    fun `should propagate exception from repository flow`() = runTest {
        val expectedException = RuntimeException("Search failed")

        every {
            repository.searchProducts("phone")
        } returns flow {
            throw expectedException
        }

        val thrownException = try {
            useCase(query = "phone").first()
            null
        } catch (exception: RuntimeException) {
            exception
        }

        assertEquals(expectedException, thrownException)
    }

    private fun createProduct() = Product(
        id = 1,
        title = "Phone",
        description = "Test phone",
        category = "smartphones",
        price = 299.99,
        discountPercentage = 10.0,
        rating = 4.5,
        stock = 10,
        tags = listOf("smartphones", "phone"),
        brand = "Test Brand",
        sku = "PHONE-001",
        weight = 0.2,
        dimensions = ProductDimensions(
            width = 70.0,
            height = 140.0,
            depth = 8.0
        ),
        warrantyInformation = "1 year warranty",
        shippingInformation = "Ships in 1-2 business days",
        availabilityStatus = "In Stock",
        reviews = listOf(
            ProductReview(
                rating = 5,
                comment = "Great phone",
                date = "2026-01-01",
                reviewerName = "John Doe",
                reviewerEmail = "john@example.com"
            )
        ),
        returnPolicy = "30 days return policy",
        minimumOrderQuantity = 1,
        images = listOf("https://example.com/phone.jpg"),
        thumbnail = "https://example.com/phone.jpg"
    )
}