package com.shopping.products.domain.usecase

import androidx.paging.PagingData
import com.shopping.products.domain.model.Product
import com.shopping.products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(private val repository: ProductsRepository) {

    operator fun invoke(query: String): Flow<PagingData<Product>> =
        repository.searchProducts(query = query)
}
