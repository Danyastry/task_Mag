package com.example.task_magnise.data.model.instruments

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("base_currency") val baseCurrency: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("kind") val kind: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("tick_size") val tickSize: Double
)