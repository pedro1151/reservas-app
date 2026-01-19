package com.optic.pramosfootballappz.domain.useCase.team.fixture.cache

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class SaveFixturesCacheUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.precacheFixturesAroundToday()
}