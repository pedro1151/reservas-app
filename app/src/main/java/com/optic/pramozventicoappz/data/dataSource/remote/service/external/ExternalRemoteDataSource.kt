package com.optic.pramozventicoappz.data.dataSource.remote.service.external

import com.optic.pramozventicoappz.domain.model.auth.AuthResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayPurchaseResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayVerifyRequest
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayVerifyResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.HasEntitlementResponse
import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.UserEntitlementResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface ExternalRemoteDataSource {

    suspend fun login(id_token: String): Response<AuthResponse>

    // GOOGLE PLAY BILLIGNS

    suspend fun googlePlayVerifyPurchase(
        request: GooglePlayVerifyRequest
    ): Response<GooglePlayVerifyResponse>

    suspend fun getUserPurchases(
         onlyActive:Boolean,
         limit: Int,
         offset:Int
    ): Response<List<GooglePlayPurchaseResponse>>

    suspend fun getUserEntitlements(
       onlyActive:Boolean
    ): Response<List<UserEntitlementResponse>>

    suspend fun getMyEntitlement(
      entitlement:String  //STANDARD | PRO | GOLD
    ): Response<HasEntitlementResponse>


}