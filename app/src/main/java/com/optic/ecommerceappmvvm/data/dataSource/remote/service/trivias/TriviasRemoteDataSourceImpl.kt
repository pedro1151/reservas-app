package com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias


import com.optic.ecommerceappmvvm.domain.model.trivias.SimilarPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.trivias.game.GameResponse
import com.optic.ecommerceappmvvm.domain.model.trivias.game.dificulty.GameDificulty
import com.optic.ecommerceappmvvm.domain.model.trivias.guessplayer.GuessPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.trivias.score.GameScoreCreate
import com.optic.ecommerceappmvvm.domain.model.trivias.score.GameScoreResponse
import retrofit2.Response

class TriviasRemoteDataSourceImpl (private val triviasService: TriviasService) : TriviasRemoteDataSource{
    override suspend fun genSimilarPlayers(): Response<SimilarPlayerResponse> = triviasService.getSimilarPlayers()
    override suspend fun genGames(): Response<List<GameResponse>> = triviasService.getGames()
    override suspend fun genDificultys(): Response<List<GameDificulty>> = triviasService.getDificultys()
    override suspend fun genGuessPlayer(topK: Int): Response<GuessPlayerResponse> = triviasService.getGuessPlayer(topK)
    override suspend fun createGameScore(gameScore: GameScoreCreate): Response<GameScoreResponse> = triviasService.createGameScore(gameScore)

}