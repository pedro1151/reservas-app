package com.optic.ecommerceappmvvm.domain.useCase.trivias

import com.optic.ecommerceappmvvm.domain.repository.TriviasRepository


class GetSimilarPlayers constructor(private val repository: TriviasRepository) {
    suspend operator fun invoke() = repository.getSimilarPlayers()
}