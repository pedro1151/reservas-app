package com.optic.ecommerceappmvvm.domain.useCase.team.fixture

import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetFixturesByRoundUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(leagueId:Int, season: Int, round: String) = repository.getFixturesByRound(leagueId, season, round)
}