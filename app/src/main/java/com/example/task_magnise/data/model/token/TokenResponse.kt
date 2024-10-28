package com.example.task_magnise.data.model.token

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token") val access_token: String,
    @SerializedName("expires_in") val expires_in: Int,
    @SerializedName("refresh_token") val refresh_token: String,
    @SerializedName("token_type") val token_type: String,
    @SerializedName("not-before-policy") val not_before_policy: Int,
    @SerializedName("session_state") val session_state: String,
    @SerializedName("scope") val scope: String
)
