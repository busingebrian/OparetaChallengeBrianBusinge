package com.leroy.oparetachallenge.utils

import androidx.lifecycle.LiveData
import com.leroy.oparetachallenge.api.ApiResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        check(rawObservableType == ApiResponse::class.java) { "Type must be resource" }
        check(observableType is ParameterizedType) { "Resource must be parameterized" }
        val bodyType = getParameterUpperBound(0, observableType)
        return LiveDataCallAdapter<Any>(bodyType)
    }
}