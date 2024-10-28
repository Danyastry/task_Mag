package com.example.task_magnise.data.interceptor

import com.example.task_magnise.data.Data.TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class Interceptor(private val authTokenProvider: AuthTokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = authTokenProvider.getAuthToken()

        val newRequest = request.newBuilder()
            .header("Authorization", "$TOKEN $token")
            .build()

        return chain.proceed(newRequest)
    }
}