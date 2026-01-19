package com.optic.pramosfootballappz.domain.useCase.team

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetLeaguesUseCase constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(name: String, type: String, countryName: String) = repository.getLeagues(name, type,countryName)
}