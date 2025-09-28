package com.optic.ecommerceappmvvm.data.dataSource.remote.service.trivias

import com.optic.ecommerceappmvvm.domain.model.trivias.SimilarPlayerResponse
import retrofit2.Response
import retrofit2.http.GET

interface TriviasService {


        // En tu interface AuthService:
        @GET("trivias/gensimilar/players")
        suspend fun getSimilarPlayers(
        ): Response<SimilarPlayerResponse>

    }
