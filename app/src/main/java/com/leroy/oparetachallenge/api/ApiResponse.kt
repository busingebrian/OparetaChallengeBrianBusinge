package com.leroy.oparetachallenge.api

import com.leroy.oparetachallenge.App
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber

sealed class ApiResponse<T> {

    companion object {
        const val STATUS_FAILED = "failed"


        fun <T>create(ex: NetworkApiException): ApiResponse<T> {
            return ApiErrorResponse(ex)
        }

        fun <T>create(response: Response<T>): ApiResponse<T> {
            try {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null || response.code() == 204) return ApiEmptyResponse()
                    if (body is SingleResponse<*>) {
                        if (body.status?.errorCode != 0)
                            throw NetworkApiException(body.status?.errorMessage ?: "Something went wrong", response.code(), STATUS_FAILED)
                    }
                    return ApiSuccessResponse(body)
                } else {
                    val errorString: String = response.errorBody()?.string() ?: response.message()
                    try {
                        val errorJson = JSONObject(errorString)
                        val statusBody = App.gson.fromJson(errorJson.getJSONObject("status").toString(), Status::class.java)
                        Timber.d("Status body: $statusBody")
                        val message: String? = statusBody?.errorMessage ?: "Please check network!"
                        if (message.isNullOrEmpty()) throw NetworkApiException(errorString, 500, STATUS_FAILED)
                        throw NetworkApiException(message, response.code(), STATUS_FAILED)
                    } catch (jex: JSONException) {
                        throw NetworkApiException(errorString, 500, STATUS_FAILED)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                val ex = if (e is NetworkApiException) e
                else
                    NetworkApiException(e.message, 500, STATUS_FAILED)
                return ApiErrorResponse(ex)
            }
        }

    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(val body: T): ApiResponse<T>()

data class ApiErrorResponse<T>(val ex: NetworkApiException): ApiResponse<T>()