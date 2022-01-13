package com.leroy.oparetachallenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.leroy.oparetachallenge.api.SingleResponse
import com.leroy.oparetachallenge.models.ConversionData
import com.leroy.oparetachallenge.models.Resource
import com.leroy.oparetachallenge.repository.ConversionsRepository

class MainViewModel: ViewModel() {

    private val conversionsRepository = ConversionsRepository.get()

    fun convertPrice(amount: Int, symbol: String, convert: String): LiveData<Resource<SingleResponse<ConversionData>>> {
        return conversionsRepository.convertPrice(amount, symbol, convert)
    }
}