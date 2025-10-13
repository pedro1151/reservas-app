package com.optic.ecommerceappmvvm.domain.useCase.trivias.guessplayer

import com.optic.ecommerceappmvvm.domain.repository.TriviasRepository


class GetGuessPlayerUC constructor(private val repository: TriviasRepository) {
    suspend operator fun invoke(topK:Int) = repository.getGuessPlayer(topK)
}