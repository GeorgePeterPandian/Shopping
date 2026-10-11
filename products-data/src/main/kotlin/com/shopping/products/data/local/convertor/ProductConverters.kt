package com.shopping.products.data.local.convertor

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.shopping.products.data.local.entity.ProductDimensionsEntity
import com.shopping.products.data.local.entity.ProductReviewEntity
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
class ProductConverters(private val json: Json) {

    @TypeConverter
    fun dimensionsToJson(value: ProductDimensionsEntity): String =
        json.encodeToString(value)

    @TypeConverter
    fun jsonToDimensions(value: String): ProductDimensionsEntity =
        json.decodeFromString<ProductDimensionsEntity>(value)

    @TypeConverter
    fun reviewsToJson(value: List<ProductReviewEntity>): String =
        json.encodeToString(value)

    @TypeConverter
    fun jsonToReviews(value: String): List<ProductReviewEntity> =
        json.decodeFromString<List<ProductReviewEntity>>(value)

    @TypeConverter
    fun stringListToJson(value: List<String>): String =
        json.encodeToString(value)

    @TypeConverter
    fun jsonToStringList(value: String): List<String> =
        json.decodeFromString<List<String>>(value)
}
