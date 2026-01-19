package com.optic.pramosfootballappz.data.dataSource.remote.service.trivias


import com.optic.pramosfootballappz.domain.model.player.Player
import com.optic.pramosfootballappz.domain.model.trivias.SimilarPlayerResponse
import com.optic.pramosfootballappz.domain.model.trivias.game.GameResponse
import com.optic.pramosfootballappz.domain.model.trivias.game.dificulty.GameDificulty
import com.optic.pramosfootballappz.domain.model.trivias.guessplayer.GuessPlayerResponse
import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreCreate
import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreResponse
import retrofit2.Response

interface TriviasRemoteDataSource {

    suspend fun genSimilarPlayers(): Response<SimilarPlayerResponse>
    suspend fun genGames(): Response<List<GameResponse>>
    suspend fun genDificultys(): Response<List<GameDificulty>>
    suspend fun genGuessPlayer(topK: Int): Response<GuessPlayerResponse>
    suspend fun createGameScore(gameScore: GameScoreCreate): Response<GameScoreResponse>

    suspend fun getSuggestedPlayers(limit: Int): Response<List<Player>>

}