package com.optic.ecommerceappmvvm.domain.useCase.trivias

import com.optic.ecommerceappmvvm.domain.useCase.trivias.games.score.CreateGameScoreUC
import com.optic.ecommerceappmvvm.domain.useCase.trivias.guessplayer.GetGuessPlayerUC
import com.optic.ecommerceappmvvm.domain.useCase.trivias.players.GetSuggestedPlayersUC

data class TriviasUseCase (
    val getSimilarPlayers: GetSimilarPlayers,
    val getGamesUC: GetGamesUC,
    val getDificultysUC: GetDificultysUC,

    //guess player
    val getGuessPlayerUC: GetGuessPlayerUC,
    //score
    val createGameScoreUC: CreateGameScoreUC,

    // players sugeridos
    val getSuggestedPlayersUC: GetSuggestedPlayersUC
)