package com.leroy.oparetachallenge.api

import com.leroy.oparetachallenge.App
import org.json.JSONException
import org.json.JSONObject

class NetworkApiException(message: String?, cause: Throwable?, var statusCode: Int, var status: String?) : Exception(message, cause) {

    constructor(message: String?, status: String?) : this(message, 500, status) {}
    constructor(message: String?, statusCode: Int, status: String?) : this(message, null, statusCode, status) {}

    override fun toString(): String {
        return App.gson.toJson(this)
    }

    fun toJsonObject(): JSONObject? {
        try {
            return JSONObject(toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        @JvmStatic
        fun createFromException(e: Exception): NetworkApiException {
            return if (e is NetworkApiException) e else NetworkApiException(e.message, 500, Resolvable.STATUS_FAILED)
        }
    }

}