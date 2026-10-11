package com.shopping.demo.products.presentation

import com.shopping.products.domain.model.Product

data class ProductsUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true,
    val searchQuery: String = ""
)