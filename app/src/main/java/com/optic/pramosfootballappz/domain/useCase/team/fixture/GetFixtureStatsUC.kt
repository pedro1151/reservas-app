package com.optic.pramosfootballappz.domain.useCase.team.fixture

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetFixtureStatsUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(id: Int) = repository.getFixtureStats(id)
}