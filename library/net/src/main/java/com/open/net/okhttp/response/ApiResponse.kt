package com.open.net.okhttp.response

import kotlinx.serialization.Serializable
import retrofit2.Response

@Serializable
data class ApiResponse<T>(

    val code: Int,

    val msg: String?,

    val data: T?,

    var isSuccessful: Boolean = true
)


fun <T> onFailureResponse(throwable: Throwable): T? {
    return ApiResponse<T>(
        -1,
        throwable.message ?: "",
        null,
        false
    ) as T
}

fun <T> onSuccessResponse(response: Response<T>): T? {
    val result: T? = response.body()
    if (result is ApiResponse<*>) {
        result.isSuccessful = (result.code == 0)
    }
    return result
}

