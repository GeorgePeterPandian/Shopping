
package com.shopping.demo.products.presentation

import androidx.paging.PagingData
import com.shopping.products.domain.usecase.GetProductsUseCase
import com.shopping.products.domain.usecase.SearchProductsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var searchProductsUseCase: SearchProductsUseCase
    private lateinit var viewModel: ProductsViewModel

    private var collectionJob: Job? = null

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getProductsUseCase = mockk()
        searchProductsUseCase = mockk()
    }

    @After
    fun tearDown() {
        // Do not manually cancel viewModelScope.
        // Cancelling the ViewModel's own scope bypasses normal lifecycle ownership.
        collectionJob?.cancel()
        Dispatchers.resetMain()
    }

    private fun TestScope.createViewModel() {
        viewModel = ProductsViewModel(
            getProductsUseCase = getProductsUseCase,
            searchProductsUseCase = searchProductsUseCase
        )
    }

    private fun TestScope.startCollectingProducts() {
        collectionJob = backgroundScope.launch {
            viewModel.products.collect { }
        }
    }

    @Test
    fun `should load initial products immediately`() = runTest {
        every { getProductsUseCase() } returns flowOf(PagingData.empty())

        createViewModel()
        startCollectingProducts()

        // The initial blank query should not wait for the search debounce.
        runCurrent()

        verify(exactly = 1) { getProductsUseCase() }
    }

    @Test
    fun `should search products successfully`() = runTest {
        every { getProductsUseCase() } returns flowOf(PagingData.empty())

        every { searchProductsUseCase("phone") } returns flowOf(PagingData.empty())

        createViewModel()
        startCollectingProducts()
        runCurrent()

        viewModel.searchProducts("phone")

        advanceTimeBy(500.milliseconds)
        runCurrent()

        assertEquals("phone", viewModel.uiState.value.searchQuery)

        verify(exactly = 1) { searchProductsUseCase("phone") }
    }

    @Test
    fun `should debounce search queries`() = runTest {
        every { getProductsUseCase() } returns flowOf(PagingData.empty())

        every { searchProductsUseCase("phone") } returns flowOf(PagingData.empty())

        createViewModel()
        startCollectingProducts()
        runCurrent()

        viewModel.searchProducts("p")
        advanceTimeBy(100.milliseconds)
        runCurrent()

        viewModel.searchProducts("ph")
        advanceTimeBy(100.milliseconds)
        runCurrent()

        viewModel.searchProducts("phone")
        advanceTimeBy(499.milliseconds)
        runCurrent()

        // The final query should not execute before the debounce expires.
        verify(exactly = 0) { searchProductsUseCase("phone") }

        advanceTimeBy(1.milliseconds)
        runCurrent()

        verify(exactly = 1) { searchProductsUseCase("phone") }

        verify(exactly = 0) { searchProductsUseCase("p") }

        verify(exactly = 0) { searchProductsUseCase("ph") }
    }

    @Test
    fun `should reload initial products when search is cleared`() = runTest {
        every {
            getProductsUseCase()
        } returns flowOf(PagingData.empty())

        every { searchProductsUseCase("phone") } returns flowOf(PagingData.empty())

        createViewModel()
        startCollectingProducts()
        runCurrent()

        viewModel.searchProducts("phone")
        advanceTimeBy(500.milliseconds)
        runCurrent()

        viewModel.searchProducts("")
        runCurrent()

        assertEquals("", viewModel.uiState.value.searchQuery)

        // Initial load and cleared search.
        verify(exactly = 2) { getProductsUseCase() }

        verify(exactly = 1) { searchProductsUseCase("phone") }
    }
}
