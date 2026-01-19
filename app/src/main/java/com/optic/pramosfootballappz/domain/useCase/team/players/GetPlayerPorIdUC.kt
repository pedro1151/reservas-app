package com.optic.pramosfootballappz.domain.useCase.team.players


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetPlayerPorIdUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(playerId:Int) = repository.getPlayerPorId(playerId)
}