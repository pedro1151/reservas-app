package com.optic.ecommerceappmvvm.domain.useCase.team.fixture

import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetNoFollowFixturesUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(season: Int, date: String) = repository.getNoFollowFixtures(season, date)
}