package com.optic.pramosfootballappz.domain.useCase.team.players


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetAllPlayersUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.getallPlayers()
}