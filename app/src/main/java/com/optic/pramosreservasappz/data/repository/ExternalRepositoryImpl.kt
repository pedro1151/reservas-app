package com.optic.pramosreservasappz.data.repository

import com.optic.pramosreservasappz.data.dataSource.remote.service.external.ExternalRemoteDataSource
import com.optic.pramosreservasappz.domain.model.AuthResponse
import com.optic.pramosreservasappz.domain.repository.ExternalRepository
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.domain.util.ResponseToRequest

class ExternalRepositoryImpl(
    private val externalRemoteDataSource: ExternalRemoteDataSource
): ExternalRepository {

    override suspend fun login(id_token: String): Resource<AuthResponse> = ResponseToRequest.send(
        externalRemoteDataSource.login(id_token)
    )

}