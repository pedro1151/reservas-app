package com.optic.ecommerceappmvvm.domain.useCase.team.fixture

import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetFixtureLineupsUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(id: Int) = repository.getFixtureLineups(id)
}