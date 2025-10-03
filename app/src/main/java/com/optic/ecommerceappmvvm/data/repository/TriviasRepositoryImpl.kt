package com.optic.ecommerceappmvvm.data.repository

import com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias.TriviasRemoteDataSource
import com.optic.ecommerceappmvvm.domain.model.trivias.SimilarPlayerResponse
import com.optic.ecommerceappmvvm.domain.model.trivias.game.GameResponse
import com.optic.ecommerceappmvvm.domain.repository.TriviasRepository
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.domain.util.ResponseToRequest
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

}