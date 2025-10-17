package com.optic.ecommerceappmvvm.domain.useCase.trivias.games.score

import com.optic.ecommerceappmvvm.domain.model.trivias.score.GameScoreCreate
import com.optic.ecommerceappmvvm.domain.repository.TriviasRepository


class CreateGameScoreUC constructor(private val repository: TriviasRepository) {
    suspend operator fun invoke(gameScoreCreate: GameScoreCreate) = repository.createGameScore(gameScoreCreate)
}