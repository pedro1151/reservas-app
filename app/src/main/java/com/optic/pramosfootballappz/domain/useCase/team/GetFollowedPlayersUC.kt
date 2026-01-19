package com.optic.pramosfootballappz.domain.useCase.team

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetFollowedPlayersUC constructor(private val repository: TeamRepository) {
    operator fun invoke() = repository.getFollowedPlayers()
}