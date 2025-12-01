package com.optic.ecommerceappmvvm.domain.useCase.team.fixture.cache

import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class SaveFixturesCacheUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.precacheFixturesAroundToday()
}