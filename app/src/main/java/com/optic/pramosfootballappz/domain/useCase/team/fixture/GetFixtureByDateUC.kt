package com.optic.pramosfootballappz.domain.useCase.team.fixture

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetFixtureByDateUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(date: String, limit: Int) = repository.getFixturesByDate(date, limit)
}