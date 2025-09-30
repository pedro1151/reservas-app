package com.optic.ecommerceappmvvm.domain.useCase.team.fixture

import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetCountryFixturesUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.getCountryFixtures()
}