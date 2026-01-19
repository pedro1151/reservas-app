package com.optic.pramosfootballappz.domain.useCase.team

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetPlayerStatsUseCase constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(playerId: Int) = repository.getPlayerStats(playerId)
}