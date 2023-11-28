package com.open.serialization

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Converter

object JsonClient {

    val jsonClient: Json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true

        }
    }

    fun createFactory(contentType: MediaType): Converter.Factory =
        jsonClient.asConverterFactory(contentType)


    inline fun <reified T> fromJson(value: String?): T? =
        if (value?.isNotEmpty() == true) jsonClient.decodeFromString(value) else null


    inline fun <reified T> toJson(value: T?): String =
        jsonClient.encodeToString(value)
}