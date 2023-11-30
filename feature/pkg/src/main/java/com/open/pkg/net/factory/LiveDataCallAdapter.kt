package com.open.pkg.net.factory

import androidx.lifecycle.LiveData
import com.open.pkg.net.response.BaseResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<T>(private val responseType: Type) : CallAdapter<T, LiveData<T>> {
    override fun adapt(call: Call<T>): LiveData<T> {
        return object : LiveData<T>() {
            private val started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            postValue(BaseResponse().onFailureResponse(t))
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            postValue(BaseResponse().onSuccessResponse(response))
                        }

                    })
                }
            }
        }
    }

    override fun responseType() = responseType
}

