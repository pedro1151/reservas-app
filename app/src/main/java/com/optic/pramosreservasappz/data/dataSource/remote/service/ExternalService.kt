package com.optic.pramosreservasappz.data.dataSource.remote.service

import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.model.external.GoogleAuthRequest
import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.GooglePlayPurchaseResponse
import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.GooglePlayVerifyRequest
import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.GooglePlayVerifyResponse
import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.HasEntitlementResponse
import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.UserEntitlementResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ExternalService {
    /* Se utiliza Response de retrofit, en los archivos de servicios */
    @POST("external-r/google-login")
    suspend fun login(
        @Body googleAuthRequest: GoogleAuthRequest
    ): Response<AuthResponse>

    // google play billigns

    @POST("external-r/payments/google-play/verify")
    suspend fun googlePlayVerifyPurchase(
        @Body request: GooglePlayVerifyRequest
    ): Response<GooglePlayVerifyResponse>

    @GET("external-r/payments/google-play/me/purchases")
    suspend fun getUserPurchases(
        @Query("only_active") onlyActive:Boolean,
        @Query("limit") limit: Int,
        @Query("offset") offset:Int
    ): Response<List<GooglePlayPurchaseResponse>>

    @GET("external-r/payments/google-play/me/entitlements")
    suspend fun getUserEntitlements(
        @Query("only_active") onlyActive:Boolean
    ): Response<List<UserEntitlementResponse>>


    @GET("external-r/payments/google-play/me/has-entitlement")
    suspend fun getMyEntitlement(
        @Query("entitlement") entitlement:String  //STANDARD | PRO | GOLD
    ): Response<HasEntitlementResponse>




}