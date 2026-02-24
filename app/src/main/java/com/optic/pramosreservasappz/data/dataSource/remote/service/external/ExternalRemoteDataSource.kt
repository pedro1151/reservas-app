package com.optic.pramosreservasappz.data.dataSource.remote.service.external

import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import retrofit2.Response

interface ExternalRemoteDataSource {

    suspend fun login(id_token: String): Response<AuthResponse>

}