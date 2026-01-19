package com.optic.pramosfootballappz.data.repository

import com.optic.pramosfootballappz.data.dataSource.remote.service.external.ExternalRemoteDataSource
import com.optic.pramosfootballappz.domain.model.AuthResponse
import com.optic.pramosfootballappz.domain.repository.ExternalRepository
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.domain.util.ResponseToRequest

class ExternalRepositoryImpl(
    private val externalRemoteDataSource: ExternalRemoteDataSource
): ExternalRepository {

    override suspend fun login(id_token: String): Resource<AuthResponse> = ResponseToRequest.send(
        externalRemoteDataSource.login(id_token)
    )

}