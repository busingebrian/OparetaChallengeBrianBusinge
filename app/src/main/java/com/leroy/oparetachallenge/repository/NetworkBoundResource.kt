package com.leroy.oparetachallenge.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.leroy.oparetachallenge.AppExecutors
import com.leroy.oparetachallenge.api.ApiEmptyResponse
import com.leroy.oparetachallenge.api.ApiErrorResponse
import com.leroy.oparetachallenge.api.ApiResponse
import com.leroy.oparetachallenge.api.ApiSuccessResponse
import com.leroy.oparetachallenge.models.Resource

abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val usesCache: Boolean) {

    private val result = MediatorLiveData<Resource<ResultType>>()
    private val appExecutors = AppExecutors.get()

    init {
        fetchFromNetwork()
    }

    private fun fetchFromNetwork() {
        val dbSource = loadFromDb()
        if (usesCache && shouldLoadFromCache()) {
            result.addSource(dbSource) {
                result.removeSource(dbSource)
                setValue(Resource.success(it, fromCache = true))
            }
        } else {
            result.value = Resource.loading(null)
            val apiResponse = createCall()
            result.addSource(apiResponse) {response ->
                result.removeSource(apiResponse)
                appExecutors.networkIO.execute {
                    when (response) {
                        is ApiSuccessResponse -> {
                            val processedResponse = processResponse(response)
                            saveCallResult(processedResponse)
                            appExecutors.mainThread.execute {
                                result.addSource(convertToLiveData(processedResponse)) {
                                    setValue(Resource.success(it, false))
                                }
                            }
                        }
                        is ApiEmptyResponse -> {
                            appExecutors.mainThread.execute {
                                result.addSource(loadFromDb()) {
                                    setValue(Resource.success(it, fromCache = true))
                                }
                            }
                        }
                        is ApiErrorResponse -> {
                            appExecutors.mainThread.execute {
                                result.addSource(loadFromDb()) {
                                    setValue(Resource.error(it, response.ex))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(value: Resource<ResultType>) {
        appExecutors.mainThread.execute {
            if (result.value != value) result.value = value
        }
    }



    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    protected open fun saveCallResult(result: RequestType) {
    }

    @MainThread
    protected open fun shouldLoadFromCache(): Boolean {
        return false
    }

    @MainThread
    protected open fun loadFromDb(): LiveData<ResultType> {
        if (!usesCache) return MutableLiveData()
        throw IllegalAccessException("Must set uses cache to true and not call super if attempting to load from db ")
    }

    @MainThread
    protected open fun convertToLiveData(requestType: RequestType): LiveData<ResultType> {
        if (usesCache) return MutableLiveData()
        throw IllegalAccessException("Must set uses cache to false and not call super if not loading from db ")
    }

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    companion object {

        @JvmStatic
        fun <T> convertResultToLiveData(item: T): MutableLiveData<T> {
            val liveData = MutableLiveData<T>()
            liveData.value = item
            return liveData
        }
    }
}