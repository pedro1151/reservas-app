package com.optic.pramosfootballappz.domain.useCase.team.cache


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class SyncCacheUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.syncCached()
}