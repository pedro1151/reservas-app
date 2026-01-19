package com.optic.pramosfootballappz.domain.useCase.team.equipos

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetSuggestedTeamsUC constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(limit: Int) = repository.getSuggestedTeams(limit)
}