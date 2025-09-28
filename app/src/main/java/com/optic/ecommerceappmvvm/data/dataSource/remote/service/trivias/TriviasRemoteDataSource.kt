package com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias


import com.optic.ecommerceappmvvm.domain.model.AuthResponse
import com.optic.ecommerceappmvvm.domain.model.User
import com.optic.ecommerceappmvvm.domain.model.trivias.SimilarPlayerResponse
import retrofit2.Response

interface TriviasRemoteDataSource {

    suspend fun genSimilarPlayers(): Response<SimilarPlayerResponse>

}