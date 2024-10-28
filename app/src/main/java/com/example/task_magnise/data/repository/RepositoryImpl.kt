package com.example.task_magnise.data.repository

import com.example.task_magnise.data.api.ApiResponse
import com.example.task_magnise.data.model.instruments.Instruments
import com.example.task_magnise.data.model.bars.BarsResponse
import com.example.task_magnise.data.model.token.TokenResponse
import com.example.task_magnise.data.webSocket.WebSocketClient
import com.example.task_magnise.domain.model.State
import com.example.task_magnise.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class RepositoryImpl(
    private val apiResponse: ApiResponse,
    private val webSocketClient: WebSocketClient,
    private val realTimeDataChannel: MutableSharedFlow<State<String>>
) : Repository {

    override fun connectToWebSocket(token: String) { webSocketClient.connect(token) }

    override fun disconnectWebSocket() { webSocketClient.disconnect() }

    override fun observeRealTimeData(token: String): Flow<State<String>> = realTimeDataChannel

    override suspend fun getToken(grantType: String, clientId: String, username: String, password: String): Flow<State<TokenResponse>> =
        safeCall { apiResponse.getToken(grantType, clientId, username, password) }

    override suspend fun getListInstruments(provider: String, kind: String): Flow<State<Instruments>> =
        safeCall { apiResponse.getListInstruments(provider, kind) }

    override suspend fun getBars(instrument: String, provider: String, interval: String, periodicity: String, barsCount: String): Flow<State<BarsResponse>> =
        safeCall { apiResponse.getBars(instrument, provider, interval, periodicity, barsCount) }

    private fun <T> safeCall(apiCall: suspend () -> T): Flow<State<T>> = flow {
        emit(State.Loading)
        try {
            emit(State.Success(apiCall()))
        } catch (e: HttpException) {
            emit(State.Error(e.message.toString()))
        } catch (e: IOException) {
            emit(State.Error(e.message.toString()))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }
}