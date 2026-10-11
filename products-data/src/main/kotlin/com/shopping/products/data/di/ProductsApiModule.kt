package com.shopping.products.data.di

import com.shopping.products.data.datasource.ProductsRemoteDataSource
import com.shopping.products.data.datasource.ProductsRemoteDataSourceImpl
import com.shopping.products.data.repository.ProductsRepositoryImpl
import com.shopping.products.data.service.ProductsApi
import com.shopping.products.domain.repository.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductsApiModule {
    @Provides
    @Singleton
    fun provideProductsApi(retrofit: Retrofit): ProductsApi =
        retrofit.create(ProductsApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductsBindingModule {

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(implementation: ProductsRemoteDataSourceImpl): ProductsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindProductsRepository(implementation: ProductsRepositoryImpl): ProductsRepository
}