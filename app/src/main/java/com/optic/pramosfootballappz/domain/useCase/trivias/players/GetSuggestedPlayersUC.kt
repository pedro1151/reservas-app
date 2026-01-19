package com.optic.pramosfootballappz.domain.useCase.trivias.players

import com.optic.pramosfootballappz.domain.repository.TriviasRepository


class GetSuggestedPlayersUC constructor(private val repository: TriviasRepository) {
    suspend operator fun invoke(limit:Int) = repository.getSuggestedPlayers(limit)
}