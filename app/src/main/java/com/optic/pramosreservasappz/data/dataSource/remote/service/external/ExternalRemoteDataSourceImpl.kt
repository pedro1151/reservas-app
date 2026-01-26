package com.optic.pramosreservasappz.data.dataSource.remote.service.external

import com.optic.pramosreservasappz.data.dataSource.remote.service.ExternalService
import com.optic.pramosreservasappz.domain.model.external.GoogleAuthRequest

class ExternalRemoteDataSourceImpl(private val externalService: ExternalService): ExternalRemoteDataSource {


    override suspend fun login(id_token: String) = externalService.login(GoogleAuthRequest( id_token))


}