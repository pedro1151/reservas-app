package com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias


import com.optic.ecommerceappmvvm.domain.model.trivias.SimilarPlayerResponse
import retrofit2.Response

class TriviasRemoteDataSourceImpl (private val triviasService: TriviasService) : TriviasRemoteDataSource{
    override suspend fun genSimilarPlayers(): Response<SimilarPlayerResponse> = triviasService.getSimilarPlayers()

}