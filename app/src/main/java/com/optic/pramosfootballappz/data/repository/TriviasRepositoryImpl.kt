package com.optic.pramosfootballappz.data.repository

import com.optic.pramosfootballappz.data.dataSource.remote.service.trivias.TriviasRemoteDataSource
import com.optic.pramosfootballappz.domain.model.player.Player
import com.optic.pramosfootballappz.domain.model.trivias.SimilarPlayerResponse
import com.optic.pramosfootballappz.domain.model.trivias.game.GameResponse
import com.optic.pramosfootballappz.domain.model.trivias.game.dificulty.GameDificulty
import com.optic.pramosfootballappz.domain.model.trivias.guessplayer.GuessPlayerResponse
import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreCreate
import com.optic.pramosfootballappz.domain.model.trivias.score.GameScoreResponse
import com.optic.pramosfootballappz.domain.repository.TriviasRepository
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.domain.util.ResponseToRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TriviasRepositoryImpl (
    private val triviasRemoteDataSource: TriviasRemoteDataSource
): TriviasRepository{
    override suspend fun getSimilarPlayers(): Flow<Resource<SimilarPlayerResponse>> = flow{
        emit(
            ResponseToRequest.send(
                triviasRemoteDataSource.genSimilarPlayers()
            )
        )
    }

    override suspend fun getGames(): Flow<Resource<List<GameResponse>>> = flow{
        emit(
            ResponseToRequest.send(
                triviasRemoteDataSource.genGames()
            )
        )
    }

    override suspend fun getDificultys(): Flow<Resource<List<GameDificulty>>> = flow {
        emit(
            ResponseToRequest.send(
                triviasRemoteDataSource.genDificultys()
            )
        )
    }

    override suspend fun getGuessPlayer(topK: Int): Flow<Resource<GuessPlayerResponse>> = flow {
        emit(
            ResponseToRequest.send(
                triviasRemoteDataSource.genGuessPlayer(topK)
            )
        )
    }

    override suspend fun createGameScore(gameScoreCreate: GameScoreCreate): Flow<Resource<GameScoreResponse>> = flow {
        emit(
            ResponseToRequest.send(
                triviasRemoteDataSource.createGameScore(gameScoreCreate)
            )
        )
    }

    override suspend fun getSuggestedPlayers(limit: Int): Flow<Resource<List<Player>>> = flow {
        emit(
            ResponseToRequest.send(
                triviasRemoteDataSource.getSuggestedPlayers(limit)
            )
        )
    }

}