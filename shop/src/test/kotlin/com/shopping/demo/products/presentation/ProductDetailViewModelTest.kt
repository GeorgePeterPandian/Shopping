package com.shopping.demo.products.presentation

import com.shopping.products.domain.model.Product
import com.shopping.products.domain.model.ProductDimensions
import com.shopping.products.domain.model.ProductReview
import com.shopping.products.domain.usecase.GetProductUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getProductUseCase: GetProductUseCase
    private lateinit var viewModel: ProductDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getProductUseCase = mockk()
        viewModel = ProductDetailViewModel(getProductUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProduct should expose product successfully`() = runTest {
        val product = Product(
            id = 1,
            title = "Test Product",
            description = "Test description",
            category = "beauty",
            price = 99.99,
            discountPercentage = 10.0,
            rating = 4.5,
            stock = 10,
            tags = listOf("beauty", "skincare"),
            brand = "Test Brand",
            sku = "TEST-001",
            weight = 0.5,
            dimensions = ProductDimensions(
                width = 10.0, height = 20.0, depth = 5.0
            ),
            warrantyInformation = "1 year warranty",
            shippingInformation = "Ships in 1-2 business days",
            availabilityStatus = "In Stock",
            reviews = listOf(
                ProductReview(
                    rating = 5,
                    comment = "Great product",
                    date = "2026-01-01",
                    reviewerName = "John Doe",
                    reviewerEmail = "john@example.com"
                )
            ),
            returnPolicy = "30 days return policy",
            minimumOrderQuantity = 1,
            images = listOf(
                "https://example.com/product.jpg"
            ),
            thumbnail = "https://example.com/product.jpg"
        )

        coEvery {
            getProductUseCase(1)
        } returns product

        viewModel.loadProduct(1)

        advanceUntilIdle()

        assertEquals(product, viewModel.uiState.value.product)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `loadProduct should expose error when use case fails`() = runTest {
        coEvery {
            getProductUseCase(1)
        } throws RuntimeException("Network error")

        viewModel.loadProduct(1)

        advanceUntilIdle()

        assertEquals(null, viewModel.uiState.value.product)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(
            "Network error", viewModel.uiState.value.error
        )
    }
}