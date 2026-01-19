package com.optic.pramosfootballappz.domain.useCase.team

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetallTeamUseCase constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(name:String, country: String, page:Int, size: Int) = repository.getAll(name, country, page, size)
}