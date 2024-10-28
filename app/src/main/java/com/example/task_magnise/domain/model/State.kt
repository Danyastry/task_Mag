package com.example.task_magnise.domain.model

sealed class State<out T> {
    data class Success<T>(val data: T) : State<T>()
    data class Error(val string: String) : State<Nothing>()
    data object Loading : State<Nothing>()
}