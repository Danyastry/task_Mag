package com.example.task_magnise.data.model.bars

import com.google.gson.annotations.SerializedName

data class BarsResponse(
   @SerializedName("data") val data: List<Bar>
)
