package com.example.task_magnise.data.api

import com.example.task_magnise.data.model.bars.BarsResponse
import com.example.task_magnise.data.model.instruments.Instruments
import com.example.task_magnise.data.model.token.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiResponse {

    @FormUrlEncoded
    @POST("identity/realms/fintatech/protocol/openid-connect/token")
    suspend fun getToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): TokenResponse

    @GET("api/instruments/v1/instruments")
    suspend fun getListInstruments(
        @Query("provider") provider: String,
        @Query("kind") kind: String,
    ): Instruments

    @GET("api/bars/v1/bars/count-back")
    suspend fun getBars(
        @Query("instrumentId") instrument: String,
        @Query("provider") provider: String,
        @Query("interval") interval: String,
        @Query("periodicity") periodicity: String,
        @Query("barsCount") barsCount: String,
    ): BarsResponse

}