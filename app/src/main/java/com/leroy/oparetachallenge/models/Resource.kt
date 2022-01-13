package com.leroy.oparetachallenge.models

import android.view.View
import com.leroy.oparetachallenge.api.NetworkApiException


open class Resource<out T>(val status: Status, val data: T?, fromCache: Boolean = false, val ex: NetworkApiException?) {

    fun loading(): Boolean {
        return status == Status.LOADING
    }

    val loadingToVisibility: Int
        get() {
            return if (status == Status.LOADING) View.VISIBLE else View.GONE
        }

    companion object {

        fun <T> success(data: T?, fromCache: Boolean): Resource<T> {
            return Resource(Status.SUCCESS, data, fromCache, null)
        }

        fun <T> error(data: T?, ex: NetworkApiException): Resource<T> {
            return Resource(Status.ERROR, data, false, ex)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, false, null)
        }

    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}