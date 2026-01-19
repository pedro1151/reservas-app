package com.optic.pramosfootballappz.domain.useCase.team.leagues

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetLeagueByIdUC constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(leagueId: Int) = repository.getLeagueById(leagueId)
}