package com.optic.ecommerceappmvvm.domain.useCase.trivias.players

import com.optic.ecommerceappmvvm.domain.repository.TriviasRepository


class GetSuggestedPlayersUC constructor(private val repository: TriviasRepository) {
    suspend operator fun invoke(limit:Int) = repository.getSuggestedPlayers(limit)
}