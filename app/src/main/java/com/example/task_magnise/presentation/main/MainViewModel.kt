package com.example.task_magnise.presentation.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_magnise.data.Data.BARS_COUNT
import com.example.task_magnise.data.Data.INTERVAL
import com.example.task_magnise.data.Data.PROVIDER
import com.example.task_magnise.data.interceptor.AuthTokenManager
import com.example.task_magnise.data.model.bars.BarsResponse
import com.example.task_magnise.data.model.instruments.Instruments
import com.example.task_magnise.data.model.token.TokenResponse
import com.example.task_magnise.domain.model.State
import com.example.task_magnise.domain.repository.Repository
import com.example.task_magnise.domain.useCases.GetBarsUseCase
import com.example.task_magnise.domain.useCases.GetInstrumentsUseCase
import com.example.task_magnise.domain.useCases.GetTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getTokenUseCase: GetTokenUseCase,
    private val getBarsUseCase: GetBarsUseCase,
    private val getInstrumentsUseCase: GetInstrumentsUseCase,
    private val authTokenManager: AuthTokenManager,
    private val repository: Repository
) : ViewModel() {

    private val _realTimeDataState = MutableStateFlow<State<String>>(State.Loading)
    val realTimeDataState: StateFlow<State<String>> = _realTimeDataState

    private val _barsState = MutableStateFlow<State<BarsResponse>>(State.Loading)
    val barsState: StateFlow<State<BarsResponse>> get() = _barsState

    private val _instrumentsState = MutableStateFlow<State<Instruments>>(State.Loading)
    val instrumentsState: StateFlow<State<Instruments>> get() = _instrumentsState

    private val _tokenState = MutableStateFlow<State<TokenResponse>>(State.Loading)
    private var _accessToken = MutableStateFlow<String?>(null)
    private var _selectedInstrumentId = mutableStateOf<String?>(null)

    private val _selectedPeriodicity = MutableStateFlow("minute")
    val selectedPeriodicity: StateFlow<String> get() = _selectedPeriodicity

    private fun observeRealTimeData(token: String) {
        viewModelScope.launch {
            repository.observeRealTimeData(token).collect { state ->
                _realTimeDataState.value = state
            }
        }
    }

    private fun saveAndObserveToken(token: String) {
        viewModelScope.launch {
            authTokenManager.setToken(token)
            repository.connectToWebSocket(token)
            observeRealTimeData(token)
        }
    }

    fun fetchToken(grantType: String, clientId: String, username: String, password: String) {
        viewModelScope.launch {
            getTokenUseCase(grantType, clientId, username, password).collect { state ->
                _tokenState.value = state
                if (state is State.Success) {
                    val token = state.data.access_token
                    if (token.isNotEmpty()) {
                        _accessToken.value = token
                        saveAndObserveToken(token)
                    } else {
                        _realTimeDataState.value = State.Error("Failed to obtain access token")
                    }
                }
            }
        }
    }

    fun fetchInstruments(provider: String, kind: String) {
        viewModelScope.launch {
            getInstrumentsUseCase(provider, kind).collect { state ->
                _instrumentsState.value = state
            }
        }
    }

    fun selectInstrument(instrumentId: String) {
        _selectedInstrumentId.value = instrumentId
    }

    fun fetchBars(provider: String, interval: String, periodicity: String, barsCount: String) {
        val instrumentId = _selectedInstrumentId.value
        if (instrumentId != null) {
            viewModelScope.launch {
                getBarsUseCase(instrumentId, provider, interval, periodicity, barsCount,).collect { state ->
                    _barsState.value = state
                }
            }
        } else {
            _barsState.value = State.Error("Instrument ID is null")
        }
    }

    fun setPeriodicity(newPeriodicity: String) {
        _selectedPeriodicity.value = newPeriodicity
        fetchBars(provider = PROVIDER, interval = INTERVAL, periodicity = newPeriodicity, barsCount = BARS_COUNT)
    }

}