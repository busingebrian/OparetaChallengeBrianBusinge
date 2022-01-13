package com.leroy.oparetachallenge.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class ConversionData(
    @PrimaryKey
    val id: Int = 0,
    val symbol: String? = null,
    val name: String? = null,
    val amount: Int? = null,
    val quote: MutableMap<String, ConversionQuote> = mutableMapOf(),
    var convert: String? = null,
    var updatedAt: Date
)

data class ConversionQuote(
    val price: Double? = null,
    @SerializedName("last_updated")
    val lastUpdated: Date? = null
)