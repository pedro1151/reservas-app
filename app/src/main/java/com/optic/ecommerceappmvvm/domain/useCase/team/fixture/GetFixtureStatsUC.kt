package com.optic.ecommerceappmvvm.domain.useCase.team.fixture

import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetFixtureStatsUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(id: Int) = repository.getFixtureStats(id)
}