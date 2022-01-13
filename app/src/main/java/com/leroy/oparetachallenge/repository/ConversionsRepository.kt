package com.leroy.oparetachallenge.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.leroy.oparetachallenge.App
import com.leroy.oparetachallenge.api.ApiResponse
import com.leroy.oparetachallenge.api.SingleResponse
import com.leroy.oparetachallenge.databases.ConversionsDb
import com.leroy.oparetachallenge.models.ConversionData
import com.leroy.oparetachallenge.models.Resource
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class ConversionsRepository {

    private var apiService = App.apiService
    private val conversionsDao = ConversionsDb.get().conversionsDao()

    fun convertPrice(
        amount: Int,
        symbol: String,
        convert: String
    ): LiveData<Resource<SingleResponse<ConversionData>>> {
        return object :
            NetworkBoundResource<SingleResponse<ConversionData>, SingleResponse<ConversionData>>(
                true
            ) {

            override fun shouldLoadFromCache(): Boolean {
                val lastUpdatedAt = conversionsDao.getUpdatedAt(amount, symbol, convert) ?: return false
                val now: Calendar = Calendar.getInstance()
                val lastUpdatedAtCalender: Calendar = Calendar.getInstance().apply {
                    time = lastUpdatedAt
                }
                val difference: Long = now.timeInMillis - lastUpdatedAtCalender.timeInMillis
                val minuteDifference = TimeUnit.MILLISECONDS.toMinutes(difference)
                Timber.d(">>> Minute difference: $minuteDifference")
                return minuteDifference <= 1
            }
            override fun createCall(): LiveData<ApiResponse<SingleResponse<ConversionData>>> {
                return apiService.convertPrice(amount, symbol, convert)
            }

            override fun convertToLiveData(requestType: SingleResponse<ConversionData>): LiveData<SingleResponse<ConversionData>> {
                return convertResultToLiveData(requestType)
            }

            override fun saveCallResult(result: SingleResponse<ConversionData>) {
                val conversionData = result.data ?: return
                conversionData.updatedAt = Date()
                conversionData.convert = convert
                conversionsDao.insertConversion(conversionData)
            }

            override fun loadFromDb(): LiveData<SingleResponse<ConversionData>> {
                Timber.d(">>> Loading from db")
                return Transformations.map(conversionsDao.getConversionData(amount, symbol, convert)) { conversionData ->
                    SingleResponse(data = conversionData)
                }
            }

        }.asLiveData()
    }

    companion object {
        @Volatile
        private var instance: ConversionsRepository? = null

        fun get(): ConversionsRepository {
            return instance ?: synchronized(this) {
                instance ?: ConversionsRepository()
            }
        }
    }

}