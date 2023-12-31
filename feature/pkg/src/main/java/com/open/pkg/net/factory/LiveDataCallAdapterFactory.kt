package com.open.pkg.net.factory

import androidx.lifecycle.LiveData
import com.open.net.okhttp.response.BaseResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type
import retrofit2.CallAdapter.Factory
import java.lang.reflect.ParameterizedType

class LiveDataCallAdapterFactory() : Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) return null
        //获取第一个泛型类型
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawType = getRawType(observableType)
//        if (rawType != BaseResponse.ApiResponse::class.java) {
//            throw IllegalArgumentException("LiveDataCallAdapterFactory: type must be ApiResponse")
//        }

        return LiveDataCallAdapter<Any>(observableType)
    }
}
