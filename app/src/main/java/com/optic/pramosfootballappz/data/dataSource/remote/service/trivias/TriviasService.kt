package com.optic.pramosfootballappz.data.dataSource.remote.service.trivias

import com.optic.pramosfootballappz.domain.model.player.Player
import com.optic.pramosfootballappz.domain.model.trivias.SimilarPlayerResponse
import com.optic.pramosfootballappz.domain.model.trivias.game.GameResponse
import com.optic.pramosfootballappz.domain.model.trivias.game.dificulty.GameDificulty
import com.optic.pramosfootballappz.domain.model.trivias.guessplayer.GuessPlayerResponse
import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreCreate
import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TriviasService {


        // En tu interface AuthService:
        @GET("trivias/gensimilar/players")
        suspend fun getSimilarPlayers(
        ): Response<SimilarPlayerResponse>

        // En tu interface AuthService:
        @GET("trivias/game/list")
        suspend fun getGames(
        ): Response<List<GameResponse>>

        // dificultades
        @GET("trivias/game/dificulty")
        suspend fun getDificultys(
        ): Response<List<GameDificulty>>

        @GET("trivias/players/guess/{top_k}")
        suspend fun getGuessPlayer(
                @Path("top_k") topK: Int
        ): Response<GuessPlayerResponse>


        // Guardar Score
        @POST("trivias/score/create")
        suspend fun createGameScore(
                @Body() gameScore: GameScoreCreate,
        ): Response<GameScoreResponse>

        // jugadores sugeridos
        @GET("trivias/suggested/players/{limit}")
        suspend fun getSuggestedPlayers(
                @Path("limit") limit: Int
        ): Response<List<Player>>



}
