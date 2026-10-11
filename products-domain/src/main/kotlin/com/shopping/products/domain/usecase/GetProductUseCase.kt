package com.shopping.products.domain.usecase

import com.shopping.products.domain.model.Product
import com.shopping.products.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProductUseCase @Inject constructor(private val repository: ProductsRepository) {

    suspend operator fun invoke(id: Int): Product = repository.getProduct(id)
}