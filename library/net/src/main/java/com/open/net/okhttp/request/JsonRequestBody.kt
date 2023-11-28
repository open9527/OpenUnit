package com.open.net.okhttp.request

import com.open.serialization.JsonClient
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink

class JsonRequestBody : RequestBody {
    private var mJson: String
    private var mBytes: ByteArray

    constructor(jsonObject: Any) {
        mJson = JsonClient.toJson(jsonObject)
        mBytes = mJson.toByteArray()
    }

    constructor(json: String) {
        mJson = json
        mBytes = mJson.toByteArray()
    }


    override fun contentType(): MediaType? {
        return "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    override fun writeTo(sink: BufferedSink) {
        sink.write(mBytes, 0, mBytes.size)
    }
}