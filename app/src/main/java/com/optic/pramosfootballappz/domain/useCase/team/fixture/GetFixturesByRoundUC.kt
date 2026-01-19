package com.optic.pramosfootballappz.domain.useCase.team.fixture

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetFixturesByRoundUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(leagueId:Int, season: Int, round: String) = repository.getFixturesByRound(leagueId, season, round)
}