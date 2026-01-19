package com.optic.pramosfootballappz.domain.useCase.team.fixture

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetFixtureLineupsUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(id: Int) = repository.getFixtureLineups(id)
}