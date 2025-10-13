package com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias

import com.optic.ecommerceappmvvm.domain.model.team.TeamResponse
import com.optic.ecommerceappmvvm.domain.model.trivias.SimilarPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.trivias.game.GameResponse
import com.optic.ecommerceappmvvm.domain.model.trivias.game.dificulty.GameDificulty
import com.optic.ecommerceappmvvm.domain.model.trivias.guessplayer.GuessPlayerResponse
import retrofit2.Response
import retrofit2.http.GET
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






}
