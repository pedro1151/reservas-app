package com.optic.pramosfootballappz.domain.useCase.team.fixture

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetVersusFixtureTeamUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(
        teamOneId:Int,
        teamTwoId: Int,
        leagueId: Int,
        season: Int
        ) = repository.getFixtureVersus(teamOneId, teamTwoId, leagueId,season)
}