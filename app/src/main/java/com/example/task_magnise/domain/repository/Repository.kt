package com.example.task_magnise.domain.repository

import com.example.task_magnise.data.model.instruments.Instruments
import com.example.task_magnise.data.model.bars.BarsResponse
import com.example.task_magnise.data.model.token.TokenResponse
import com.example.task_magnise.domain.model.State
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getToken(grantType: String, clientId: String, username: String, password: String): Flow<State<TokenResponse>>
    suspend fun getListInstruments(provider: String, kind: String): Flow<State<Instruments>>
    suspend fun getBars(instrument: String, provider: String, interval: String, periodicity: String, barsCount: String): Flow<State<BarsResponse>>

    fun connectToWebSocket(token: String)
    fun disconnectWebSocket()
    fun observeRealTimeData(token: String): Flow<State<String>>
}