package com.example.task_magnise.domain.useCases

import com.example.task_magnise.domain.repository.Repository

class GetBarsUseCase(private val repository: Repository) {
    suspend operator fun invoke(instrument: String, provider: String, interval: String, periodicity: String, barsCount: String) =
        repository.getBars(instrument, provider, interval, periodicity, barsCount)
}