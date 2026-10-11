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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GetProductsUseCaseTest {

    private val repository: ProductsRepository = mockk()
    private val useCase = GetProductsUseCase(repository)

    @Test
    fun `should return products from repository`() = runTest {
        val expectedPagingData = PagingData.from(listOf(createProduct()))

        every { repository.getProducts() } returns flowOf(expectedPagingData)

        val result = useCase().first()

        assertNotNull(result)

        verify(exactly = 1) { repository.getProducts() }
    }

    @Test
    fun `should return empty PagingData from repository`() = runTest {
        every { repository.getProducts() } returns flowOf(PagingData.empty<Product>())

        val result = useCase().first()

        assertNotNull(result)

        verify(exactly = 1) { repository.getProducts() }
    }

    @Test
    fun `should propagate exception from repository flow`() = runTest {
        val expectedException = RuntimeException("Repository error")

        every { repository.getProducts() } returns flow { throw expectedException }

        val thrownException = try {
            useCase().first()
            null
        } catch (exception: RuntimeException) {
            exception
        }

        assertEquals(expectedException, thrownException)
    }

    @Test
    fun `should support multiple PagingData emissions`() = runTest {
        every { repository.getProducts() } returns flowOf(
            PagingData.from(listOf(createProduct())), PagingData.empty()
        )

        val results = useCase().toList()

        assertEquals(2, results.size)
    }

    private fun createProduct() = Product(
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
        weight = 0.17,
        dimensions = ProductDimensions(
            width = 71.0, height = 146.0, depth = 8.0
        ),
        warrantyInformation = "1 year manufacturer warranty",
        shippingInformation = "Ships in 1-2 business days",
        availabilityStatus = "In Stock",
        reviews = listOf(
            ProductReview(
                rating = 5,
                comment = "Excellent phone",
                date = "2026-01-01",
                reviewerName = "John Doe",
                reviewerEmail = "john@example.com"
            )
        ),
        returnPolicy = "30 days return policy",
        minimumOrderQuantity = 1,
        images = listOf("https://example.com/image.jpg"),
        thumbnail = "https://example.com/image.jpg"
    )
}