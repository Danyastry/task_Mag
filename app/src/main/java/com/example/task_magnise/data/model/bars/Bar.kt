package com.example.task_magnise.data.model.bars

import com.google.gson.annotations.SerializedName

data class Bar(
    @SerializedName("t") val time: String,
    @SerializedName("o") val open: Double,
    @SerializedName("c") val close: Double,
    @SerializedName("h") val high: Double,
    @SerializedName("l") val low: Double,
    @SerializedName("v") val volume: Double
)
