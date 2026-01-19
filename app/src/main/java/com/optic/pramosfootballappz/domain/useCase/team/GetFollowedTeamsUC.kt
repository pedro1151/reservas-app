package com.optic.pramosfootballappz.domain.useCase.team

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetFollowedTeamsUC constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.getFollowedTeams()
}