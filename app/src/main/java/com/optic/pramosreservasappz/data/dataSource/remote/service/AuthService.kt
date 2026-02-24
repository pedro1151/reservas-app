package com.optic.pramosreservasappz.data.dataSource.remote.service

import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.model.auth.LoginRequest
import com.optic.pramosreservasappz.domain.model.auth.User
import com.optic.pramosreservasappz.domain.model.auth.LoginPlessRequest
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeRequest
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramosreservasappz.domain.model.auth.RefreshTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

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

}