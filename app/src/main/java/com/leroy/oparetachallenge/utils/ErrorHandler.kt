package com.leroy.oparetachallenge.utils

import android.widget.Toast
import com.leroy.oparetachallenge.App
import com.leroy.oparetachallenge.api.NetworkApiException


object ErrorHandler {

    fun showError(ex: NetworkApiException?) {
        ex?.printStackTrace()
        Toast.makeText(App.context, ex?.message ?: "A network error occurred", Toast.LENGTH_SHORT).show()
    }
}