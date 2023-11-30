package com.open.pkg.net.response

import com.open.serialization.DynamicLookupSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response

open class BaseResponse {
    companion object {
        private const val CODE_SUCCESS: Int = 0
    }

    fun <T> onSuccessResponse(response: Response<T>): T? {
        val result: T? = response.body()
        if (result is ApiResponse<*>) {
            result.isSuccessful = (result.code == CODE_SUCCESS)
        }
        return result
    }

    fun <T> onFailureResponse(throwable: Throwable): T? {

        return ApiResponse<T>(
            -1,
            throwable.message ?: "",
            null,
            false
        ) as T
    }

    @Serializable
    data class ApiResponse<T>(
        @SerialName("errorCode")
        val code: Int,

        @SerialName("errorMsg")
        val msg: String?,

        val data: T?,

        var isSuccessful: Boolean = false
    )
}