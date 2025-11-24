package com.optic.ecommerceappmvvm.domain.useCase.team.fixture

import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetFixtureLeagueUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(leagueId: Int, season:Int, teamId: Int) = repository.getLeagueFixture(leagueId, season, teamId)
}