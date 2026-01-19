package com.optic.pramosfootballappz.domain.useCase.team

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetPlayersUseCase constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(name: String?, page:Int, size: Int) = repository.getPlayers(name, page, size)
}