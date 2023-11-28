package com.open.net.factory

import androidx.lifecycle.LiveData
import com.open.net.okhttp.response.onFailureResponse
import com.open.net.okhttp.response.onSuccessResponse
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
                            postValue(onFailureResponse(t))
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            postValue(onSuccessResponse(response))
                        }

                    })
                }
            }
        }
    }

    override fun responseType() = responseType
}

