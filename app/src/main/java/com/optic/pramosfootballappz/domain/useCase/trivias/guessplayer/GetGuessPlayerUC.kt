package com.optic.pramosfootballappz.domain.useCase.trivias.guessplayer

import com.optic.pramosfootballappz.domain.repository.TriviasRepository


class GetGuessPlayerUC constructor(private val repository: TriviasRepository) {
    suspend operator fun invoke(topK:Int) = repository.getGuessPlayer(topK)
}