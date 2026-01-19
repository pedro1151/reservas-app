package com.optic.pramosfootballappz.domain.useCase.team.fixture

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetFixtureFollowedTeamsUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(season: Int, date: String) = repository.getFixtureFollowedTeams(season, date)
}