package com.optic.pramosfootballappz.domain.useCase.team.fixture

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetTopFiveFixtureTeamUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(teamId: Int) = repository.getTopFiveFixtureTeam(teamId)
}