package com.optic.pramosfootballappz.domain.useCase.team


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class CreateFollowedPlayerUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(
        playerId: Int,
        isAuthenticated: Boolean
    ) = repository.createFollowedPlayer(playerId, isAuthenticated)
}