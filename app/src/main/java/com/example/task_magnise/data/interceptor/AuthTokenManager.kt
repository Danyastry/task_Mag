package com.example.task_magnise.data.interceptor

class AuthTokenManager : AuthTokenProvider {
    private var token: String = ""

    fun setToken(newToken: String) {
        token = newToken
    }

    override fun getAuthToken(): String {
        return token
    }
}