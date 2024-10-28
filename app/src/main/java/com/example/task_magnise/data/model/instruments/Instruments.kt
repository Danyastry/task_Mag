package com.example.task_magnise.data.model.instruments

import com.google.gson.annotations.SerializedName

data class Instruments(
    @SerializedName("data") val data: List<Data>,
    @SerializedName("paging") val paging: Paging
)