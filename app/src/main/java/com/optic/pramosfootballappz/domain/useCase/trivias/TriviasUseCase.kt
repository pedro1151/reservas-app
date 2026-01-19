package com.optic.pramosfootballappz.domain.useCase.trivias

import com.optic.pramosfootballappz.domain.useCase.trivias.games.score.CreateGameScoreUC
import com.optic.pramosfootballappz.domain.useCase.trivias.guessplayer.GetGuessPlayerUC
import com.optic.pramosfootballappz.domain.useCase.trivias.players.GetSuggestedPlayersUC

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