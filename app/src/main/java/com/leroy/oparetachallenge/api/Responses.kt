package com.leroy.oparetachallenge.api

import com.google.gson.annotations.SerializedName
import java.util.*

data class Status(
    val timestamp: Date? = null,
    @SerializedName("error_code")
    val errorCode: Int? = null,
    @SerializedName("error_message")
    val errorMessage: String? = null
)

data class SingleResponse<T>(
    val status: Status? = null,
    val data: T?
)