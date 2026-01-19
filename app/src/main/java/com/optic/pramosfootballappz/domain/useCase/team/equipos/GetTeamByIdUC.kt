package com.optic.pramosfootballappz.domain.useCase.team.equipos

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetTeamByIdUC constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(teamId: Int) = repository.getTeamById(teamId)
}