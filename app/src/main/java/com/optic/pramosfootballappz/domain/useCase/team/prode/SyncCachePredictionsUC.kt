package com.optic.pramosfootballappz.domain.useCase.team.prode


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class SyncCachePredictionsUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.syncCachedPredictions()
}