package com.optic.pramosfootballappz.data.dataSource.remote.service

import com.optic.pramosfootballappz.domain.model.AuthResponse
import com.optic.pramosfootballappz.domain.model.external.GoogleAuthRequest

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ExternalService {
    /* Se utiliza Response de retrofit, en los archivos de servicios */
    @POST("external/google-login")
    suspend fun login(
        @Body googleAuthRequest: GoogleAuthRequest
    ): Response<AuthResponse>

}