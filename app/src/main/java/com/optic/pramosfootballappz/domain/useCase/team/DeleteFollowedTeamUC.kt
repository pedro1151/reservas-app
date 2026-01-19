package com.optic.pramosfootballappz.domain.useCase.team


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class DeleteFollowedTeamUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(teamId: Int) = repository.deleteFollowedTeam(teamId)
}