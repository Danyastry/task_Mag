package com.example.task_magnise.domain.useCases

import com.example.task_magnise.domain.repository.Repository

class GetTokenUseCase(private val repository: Repository) {
    suspend operator fun invoke(grantType: String, clientId: String, username: String, password: String) =
        repository.getToken(grantType, clientId, username, password)
}