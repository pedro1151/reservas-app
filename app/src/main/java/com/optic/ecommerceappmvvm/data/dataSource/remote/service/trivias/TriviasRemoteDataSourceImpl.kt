package com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias


import com.optic.ecommerceappmvvm.domain.model.trivias.SimilarPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.trivias.game.GameResponse
import retrofit2.Response

class TriviasRemoteDataSourceImpl (private val triviasService: TriviasService) : TriviasRemoteDataSource{
    override suspend fun genSimilarPlayers(): Response<SimilarPlayerResponse> = triviasService.getSimilarPlayers()
    override suspend fun genGames(): Response<List<GameResponse>> = triviasService.getGames()

}