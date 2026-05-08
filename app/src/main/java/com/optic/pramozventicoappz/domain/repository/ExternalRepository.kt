package com.optic.pramozventicoappz.domain.repository

import com.optic.pramozventicoappz.domain.model.auth.AuthResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayPurchaseResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayVerifyRequest
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayVerifyResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.HasEntitlementResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.UserEntitlementResponse
import com.optic.pramozventicoappz.domain.util.Resource
import retrofit2.Response

interface ExternalRepository {

    suspend fun login(id_token: String):  Resource<AuthResponse>

    // GOOGLE PLAY BILLIGNS

    suspend fun googlePlayVerifyPurchase(
        request: GooglePlayVerifyRequest
    ): Resource<GooglePlayVerifyResponse>

    suspend fun getUserPurchases(
        onlyActive:Boolean,
        limit: Int,
        offset:Int
    ): Resource<List<GooglePlayPurchaseResponse>>

    suspend fun getUserEntitlements(
        onlyActive:Boolean
    ): Resource<List<UserEntitlementResponse>>

    suspend fun getMyEntitlement(
        entitlement:String  //STANDARD | PRO | GOLD
    ): Resource<HasEntitlementResponse>

}