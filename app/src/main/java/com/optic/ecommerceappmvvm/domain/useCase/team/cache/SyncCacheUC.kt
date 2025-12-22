package com.optic.ecommerceappmvvm.domain.useCase.team.cache


import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionRequest
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class SyncCacheUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.syncCached()
}