package com.open.net

import com.open.net.factory.LiveDataCallAdapterFactory
import retrofit2.CallAdapter


object NetConfig {
    private var isDebug: Boolean = false
    private var hostUrl: String = ""
    private var callAdapterFactory: CallAdapter.Factory = LiveDataCallAdapterFactory()
    fun initialize(
        debug: Boolean,
        hostUrl: String,
        callAdapterFactory: CallAdapter.Factory = LiveDataCallAdapterFactory()
    ) {
        this.isDebug = debug
        this.hostUrl = hostUrl
        this.callAdapterFactory = callAdapterFactory
    }

    fun getDebug(): Boolean = isDebug
    fun setHostUrl(hostUrl: String) {
        this.hostUrl = hostUrl
    }

    fun getHostUrl(): String = hostUrl

    fun setCallAdapterFactory(callAdapterFactory: CallAdapter.Factory) {
        this.callAdapterFactory = callAdapterFactory
    }

    fun getCallAdapterFactory(): CallAdapter.Factory = callAdapterFactory

}