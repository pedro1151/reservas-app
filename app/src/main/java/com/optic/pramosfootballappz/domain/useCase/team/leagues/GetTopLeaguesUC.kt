package com.optic.pramosfootballappz.domain.useCase.team.leagues

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetTopLeaguesUC constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.getTopLeagues()
}