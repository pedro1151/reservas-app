package com.optic.ecommerceappmvvm.domain.useCase.trivias

import com.optic.ecommerceappmvvm.domain.useCase.trivias.guessplayer.GetGuessPlayerUC

data class TriviasUseCase (
    val getSimilarPlayers: GetSimilarPlayers,
    val getGamesUC: GetGamesUC,
    val getDificultysUC: GetDificultysUC,

    //guess player
    val getGuessPlayerUC: GetGuessPlayerUC
)