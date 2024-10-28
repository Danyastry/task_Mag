@file:Suppress("DEPRECATION")

package com.example.task_magnise.di

import com.example.task_magnise.data.Data.BASE_URL
import com.example.task_magnise.data.api.ApiResponse
import com.example.task_magnise.data.interceptor.AuthTokenManager
import com.example.task_magnise.data.interceptor.Interceptor
import com.example.task_magnise.data.repository.RepositoryImpl
import com.example.task_magnise.data.webSocket.MyWebSocketListener
import com.example.task_magnise.data.webSocket.WebSocketClient
import com.example.task_magnise.domain.model.State
import com.example.task_magnise.domain.repository.Repository
import com.example.task_magnise.domain.useCases.GetBarsUseCase
import com.example.task_magnise.domain.useCases.GetInstrumentsUseCase
import com.example.task_magnise.domain.useCases.GetTokenUseCase
import com.example.task_magnise.presentation.main.MainViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }

    single { AuthTokenManager() }

    single {
        OkHttpClient.Builder()
            .addInterceptor(Interceptor(get<AuthTokenManager>()))
            .addInterceptor(get<HttpLoggingInterceptor>())
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .connectTimeout(30L, TimeUnit.SECONDS)
            .build()
    }

    single { MyWebSocketListener(get<MutableSharedFlow<State<String>>>())}
    single { MutableSharedFlow<State<String>>(replay = 1, extraBufferCapacity = 1) }
    single { WebSocketClient(get(), get()) }

    single<Repository> { RepositoryImpl(get(), get(), get()) }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ApiResponse> { get<Retrofit>().create(ApiResponse::class.java) }

    factory { GetTokenUseCase(get()) }
    factory { GetBarsUseCase(get()) }
    factory { GetInstrumentsUseCase(get()) }

    viewModel { MainViewModel(get(), get(), get(), get(), get()) }
}