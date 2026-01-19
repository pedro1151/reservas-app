package com.optic.pramosfootballappz.domain.useCase.team.players


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetPlayerLastTeamUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(playerId: Int) = repository.getPlayerLastTeam(playerId)
}