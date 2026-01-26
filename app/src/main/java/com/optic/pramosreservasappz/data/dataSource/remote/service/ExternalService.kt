package com.optic.pramosreservasappz.data.dataSource.remote.service

import com.optic.pramosreservasappz.domain.model.AuthResponse
import com.optic.pramosreservasappz.domain.model.external.GoogleAuthRequest

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ExternalService {
    /* Se utiliza Response de retrofit, en los archivos de servicios */
    @POST("external-r/google-login")
    suspend fun login(
        @Body googleAuthRequest: GoogleAuthRequest
    ): Response<AuthResponse>

}