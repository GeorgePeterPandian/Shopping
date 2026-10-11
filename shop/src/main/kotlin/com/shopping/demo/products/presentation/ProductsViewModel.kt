package com.shopping.demo.products.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shopping.demo.products.presentation.model.ProductsUIModel
import com.shopping.demo.products.presentation.model.toUIModel
import com.shopping.products.domain.model.Product
import com.shopping.products.domain.usecase.GetProductsUseCase
import com.shopping.products.domain.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import androidx.paging.map as pagingMap

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()
    private val searchQuery = MutableStateFlow("")
    private val retryTrigger = MutableStateFlow(0)

    private val queryChanges: Flow<String> = flow {
        var previousQuery: String? = null
        combine(searchQuery, retryTrigger) { query, retry ->
            query.trim() to retry
        }.distinctUntilChanged().transformLatest { (query, _) ->
                val shouldDebounce = query != previousQuery && query.isNotBlank()
                previousQuery = query
                if (shouldDebounce) {
                    delay(500.milliseconds)
                }
                emit(query)
            }.collect { query -> emit(query) }
    }

    val products: Flow<PagingData<ProductsUIModel>> = queryChanges.flatMapLatest { query ->
            val source: Flow<PagingData<Product>> = when {
                query.isBlank() -> getProductsUseCase()
                else -> searchProductsUseCase(query)
            }

            source.onStart {
                    _uiState.value = _uiState.value.copy(error = null)
                }.catch { exception ->
                    _uiState.value = _uiState.value.copy(error = exception.message)

                    emit(PagingData.empty<Product>())
                }.map { pagingData: PagingData<Product> ->
                    pagingData.pagingMap { product: Product ->
                        product.toUIModel()
                    }
                }
        }.cachedIn(viewModelScope)

    fun searchProducts(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchQuery.value = query.trim()
    }

    fun retryProducts() {
        _uiState.value = _uiState.value.copy(error = null)
        retryTrigger.value += 1
    }
}
