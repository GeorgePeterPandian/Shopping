package com.shopping.products.domain.usecase

import androidx.paging.PagingData
import com.shopping.products.domain.model.Product
import com.shopping.products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repository: ProductsRepository) {

    operator fun invoke(): Flow<PagingData<Product>> =
        repository.getProducts()
}
