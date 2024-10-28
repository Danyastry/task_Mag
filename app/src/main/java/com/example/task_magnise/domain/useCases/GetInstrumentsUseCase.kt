package com.example.task_magnise.domain.useCases

import com.example.task_magnise.domain.repository.Repository

class GetInstrumentsUseCase(private val repository: Repository) {
    suspend operator fun invoke(provider: String, kind: String) =
        repository.getListInstruments(provider, kind)
}