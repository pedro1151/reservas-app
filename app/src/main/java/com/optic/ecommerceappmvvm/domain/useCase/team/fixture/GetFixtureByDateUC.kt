package com.optic.ecommerceappmvvm.domain.useCase.team.fixture

import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetFixtureByDateUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(date: String, limit: Int) = repository.getFixturesByDate(date, limit)
}