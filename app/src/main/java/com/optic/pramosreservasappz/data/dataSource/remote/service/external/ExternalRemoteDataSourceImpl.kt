package com.optic.pramosreservasappz.data.dataSource.remote.service.external

import com.optic.pramosreservasappz.data.dataSource.remote.service.ExternalService
import com.optic.pramosreservasappz.domain.model.external.GoogleAuthRequest
import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.GooglePlayPurchaseResponse
import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.GooglePlayVerifyRequest
import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.GooglePlayVerifyResponse
import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.HasEntitlementResponse
import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.UserEntitlementResponse
import retrofit2.Response

class ExternalRemoteDataSourceImpl(private val externalService: ExternalService): ExternalRemoteDataSource {


    override suspend fun login(id_token: String) = externalService.login(GoogleAuthRequest( id_token))

    // google play billings
    override suspend fun googlePlayVerifyPurchase(
        request: GooglePlayVerifyRequest
    ): Response<GooglePlayVerifyResponse> = externalService.googlePlayVerifyPurchase(request)

    override suspend fun getUserPurchases(
        onlyActive: Boolean,
        limit: Int,
        offset: Int
    ): Response<List<GooglePlayPurchaseResponse>> = externalService.getUserPurchases(
        onlyActive = onlyActive,
        limit = limit,
        offset = offset
    )

    override suspend fun getUserEntitlements(
        onlyActive: Boolean
    ): Response<List<UserEntitlementResponse>> = externalService.getUserEntitlements(onlyActive)

    override suspend fun getMyEntitlement(
        entitlement: String
    ): Response<HasEntitlementResponse> = externalService.getMyEntitlement(entitlement)


}