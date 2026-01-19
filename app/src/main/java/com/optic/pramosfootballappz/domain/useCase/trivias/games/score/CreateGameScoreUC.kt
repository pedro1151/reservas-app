package com.optic.pramosfootballappz.domain.useCase.trivias.games.score

import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreCreate
import com.optic.pramosfootballappz.domain.repository.TriviasRepository


class CreateGameScoreUC constructor(private val repository: TriviasRepository) {
    suspend operator fun invoke(gameScoreCreate: GameScoreCreate) = repository.createGameScore(gameScoreCreate)
}