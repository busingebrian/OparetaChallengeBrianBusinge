package com.leroy.oparetachallenge.api

import androidx.lifecycle.LiveData
import com.leroy.oparetachallenge.models.ConversionData
import okhttp3.HttpUrl
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("tools/price-conversion")
    fun convertPrice(
        @Query("amount") amount: Int,
        @Query("symbol") symbol: String,
        @Query("convert") convert: String
    ): LiveData<ApiResponse<SingleResponse<ConversionData>>>


    companion object {
        private const val HOST = "pro-api.coinmarketcap.com"
        private val host: String
            get() = HOST
        val httpUrl: String
            get() {
                val httpUrl = HttpUrl.Builder()
                    .scheme("https")
                    .host(host)
                    .addPathSegment("v1")
                    .build()
                return "$httpUrl/"
            }
    }
}