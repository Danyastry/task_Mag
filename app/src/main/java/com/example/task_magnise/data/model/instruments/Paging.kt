package com.example.task_magnise.data.model.instruments

import com.google.gson.annotations.SerializedName

data class Paging(
   @SerializedName("items") val items: Int,
   @SerializedName("page") val page: Int,
   @SerializedName("pages") val pages: Int
)