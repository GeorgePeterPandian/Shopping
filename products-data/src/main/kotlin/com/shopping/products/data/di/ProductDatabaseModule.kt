package com.shopping.products.data.di

import android.content.Context
import androidx.room.Room
import com.shopping.products.data.local.ProductDatabase
import com.shopping.products.data.local.convertor.ProductConverters
import com.shopping.products.data.local.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductDatabaseModule {

    @Provides
    @Singleton
    fun provideProductConverters(json: Json): ProductConverters = ProductConverters(json)

    @Provides
    @Singleton
    fun provideProductDatabase(
        @ApplicationContext context: Context, productConverters: ProductConverters
    ): ProductDatabase = Room.databaseBuilder(context, ProductDatabase::class.java, "products.db")
        .addTypeConverter(productConverters).build()

    @Provides
    @Singleton
    fun provideProductDao(database: ProductDatabase): ProductDao = database.productDao()
}
