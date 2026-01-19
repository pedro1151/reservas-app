package com.optic.pramosfootballappz.domain.useCase.team.fixture

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetFixtureByIdUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(id: Int) = repository.getFixtureById(id)
}