package com.optic.pramosfootballappz.domain.useCase.team.fixture

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetFixtureByRangeUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(dateStart: String, dateEnd: String) = repository.getFixturesByRange(dateStart, dateEnd)
}