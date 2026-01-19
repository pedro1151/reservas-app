package com.optic.pramosfootballappz.domain.repository


import com.optic.pramosfootballappz.domain.model.player.Player
import com.optic.pramosfootballappz.domain.model.trivias.SimilarPlayerResponse
import com.optic.pramosfootballappz.domain.model.trivias.game.GameResponse
import com.optic.pramosfootballappz.domain.model.trivias.game.dificulty.GameDificulty
import com.optic.pramosfootballappz.domain.model.trivias.guessplayer.GuessPlayerResponse
import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreCreate
import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreResponse
import com.optic.pramosfootballappz.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface TriviasRepository {
    suspend fun getSimilarPlayers(): Flow<Resource<SimilarPlayerResponse>>
    suspend fun getGames(): Flow<Resource<List<GameResponse>>>
    suspend fun getDificultys(): Flow<Resource<List<GameDificulty>>>
    suspend fun getGuessPlayer(topK: Int): Flow<Resource<GuessPlayerResponse>>
    suspend fun createGameScore(gameScoreCreate: GameScoreCreate): Flow<Resource<GameScoreResponse>>

    suspend fun getSuggestedPlayers(limit: Int): Flow<Resource<List<Player>>>

}