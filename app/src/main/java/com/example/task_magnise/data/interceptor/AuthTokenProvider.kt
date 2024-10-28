package com.example.task_magnise.data.interceptor

interface AuthTokenProvider {
    fun getAuthToken(): String
}