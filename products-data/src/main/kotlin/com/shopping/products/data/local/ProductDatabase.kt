package com.shopping.products.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shopping.products.data.local.convertor.ProductConverters
import com.shopping.products.data.local.dao.ProductDao
import com.shopping.products.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = true)
@TypeConverters(ProductConverters::class)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
