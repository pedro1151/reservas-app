package com.optic.pramozventicoappz.data.repository

import com.optic.pramozventicoappz.data.dataSource.remote.service.external.ExternalRemoteDataSource
import com.optic.pramozventicoappz.domain.model.auth.AuthResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayPurchaseResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayVerifyRequest
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayVerifyResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.HasEntitlementResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.UserEntitlementResponse
import com.optic.pramozventicoappz.domain.repository.ExternalRepository
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.domain.util.ResponseToRequest

class ExternalRepositoryImpl(
    private val externalRemoteDataSource: ExternalRemoteDataSource
): ExternalRepository {


    // google auth

    override suspend fun login(id_token: String): Resource<AuthResponse> = ResponseToRequest.send(
        externalRemoteDataSource.login(id_token)
    )


    // google play billings

    override suspend fun googlePlayVerifyPurchase(
        request: GooglePlayVerifyRequest
    ): Resource<GooglePlayVerifyResponse>  =
        ResponseToRequest.send(
            externalRemoteDataSource.googlePlayVerifyPurchase(request)
        )


    override suspend fun getUserPurchases(
        onlyActive: Boolean,
        limit: Int,
        offset: Int
    ): Resource<List<GooglePlayPurchaseResponse>> =
        ResponseToRequest.send(
            externalRemoteDataSource.getUserPurchases(
                onlyActive = onlyActive,
                limit =  limit,
                offset =  offset
            )
        )


    override suspend fun getUserEntitlements(
        onlyActive: Boolean
    ): Resource<List<UserEntitlementResponse>> =
        ResponseToRequest.send(
            externalRemoteDataSource.getUserEntitlements(onlyActive)
        )


    override suspend fun getMyEntitlement(
        entitlement: String
    ): Resource<HasEntitlementResponse> =
        ResponseToRequest.send(
            externalRemoteDataSource.getMyEntitlement(entitlement)
        )


}