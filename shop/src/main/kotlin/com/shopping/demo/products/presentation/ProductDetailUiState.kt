package com.shopping.demo.products.presentation

import com.shopping.demo.products.presentation.model.ProductsUIModel

data class ProductDetailUiState(
    val product: ProductsUIModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)