package com.optic.pramosfootballappz.data.dataSource.remote.service.external

import com.optic.pramosfootballappz.domain.model.AuthResponse
import retrofit2.Response

interface ExternalRemoteDataSource {

    suspend fun login(id_token: String): Response<AuthResponse>

}