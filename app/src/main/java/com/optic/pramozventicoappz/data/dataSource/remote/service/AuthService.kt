package com.optic.pramozventicoappz.data.dataSource.remote.service

import com.optic.pramozventicoappz.domain.model.auth.AuthResponse
import com.optic.pramozventicoappz.domain.model.auth.BasicUserResponse
import com.optic.pramozventicoappz.domain.model.auth.LoginRequest
import com.optic.pramozventicoappz.domain.model.auth.User
import com.optic.pramozventicoappz.domain.model.auth.LoginPlessRequest
import com.optic.pramozventicoappz.domain.model.auth.LoginSendCodeRequest
import com.optic.pramozventicoappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramozventicoappz.domain.model.auth.RefreshTokenRequest
import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserCollabUpdateRequest
import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramozventicoappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramozventicoappz.domain.model.response.DefaultResponse
import com.optic.pramozventicoappz.domain.model.sales.SalesStatsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {

    // http://192.168.1.15:3000/auth/login
    /*@FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<AuthResponse>

     */
    // En tu interface AuthService:
    @POST("auth-reservas/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<AuthResponse>

    @POST("auth-reservas/register")
    suspend fun register(
        @Body() user: User,
    ): Response<AuthResponse>



    // refresh token
    @POST("auth-reservas/refresh")
    suspend fun refresToken(
        @Body request: RefreshTokenRequest
    ): Response<AuthResponse>

    //Login sin password

    @POST("auth-reservas/code/request")
    suspend fun loginSendCode(
        @Body loginSendCode: LoginSendCodeRequest
    ): Response<LoginSendCodeResponse>

    @POST("auth-reservas/code/verify")
    suspend fun loginPless(
        @Body loginPlessRequest: LoginPlessRequest
    ): Response<AuthResponse>

    //business

    // pare crear un colaborador
    @POST("auth-reservas/register/collab")
    suspend fun register_collab(
        @Body() user: UserCollabCreateRequest,
    ): Response<BasicUserResponse>

    // RECUPERA lista de colaboradores o memebers
    @GET("/auth-reservas/members")
    suspend fun getBusinessMembers(
        @Query("business_id") businessId: Int,
        @Query("email") email: String,
        @Query("username") username: String
    ): Response<List<UserMemberResponse>>

    // Recuperar Business
    @GET("/auth-reservas/business")
    suspend fun getBusinessById(
        @Query("business_id") businessId: Int,
        @Query("user_id") userId: Int
    ): Response<BusinessCompleteResponse>


    // update member
    @PUT("/auth-reservas/member")
    suspend fun updateBusinessMember(
        @Body() request: UserCollabUpdateRequest,
    ): Response<DefaultResponse>


    @GET("/auth-reservas/user/{user_id}")
    suspend fun getUser(
        @Path("user_id") userId: Int,
    ): Response<UserMemberResponse>





}