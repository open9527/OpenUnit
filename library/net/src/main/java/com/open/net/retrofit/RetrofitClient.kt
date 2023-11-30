package com.open.net.retrofit

import com.open.net.NetConfig
import com.open.net.factory.LiveDataCallAdapterFactory
import com.open.net.okhttp.OkhttpClient
import com.open.serialization.JsonClient
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {
    inline fun <reified T> retrofitClient(
        baseUrl: String = NetConfig.getHostUrl(),
    ): T {
        return getRetrofitClient(baseUrl)
            .create(T::class.java)
    }

    private var retrofit: Retrofit? = null

    fun getRetrofitClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(NetConfig.getOkHttpClient() ?: OkhttpClient.okHttpClient)
                .addConverterFactory(JsonClient.createFactory("application/json".toMediaType()))
                .addCallAdapterFactory(NetConfig.getCallAdapterFactory())
                .build()
        }
        if (baseUrl != retrofit?.baseUrl().toString()) {
            retrofit = retrofit?.newBuilder()
                ?.baseUrl(baseUrl)?.build()
        }
        return retrofit!!
    }
}