package com.optic.ecommerceappmvvm.data.dataSource.remote.service

import com.optic.ecommerceappmvvm.domain.model.AuthResponse
import com.optic.ecommerceappmvvm.domain.model.LoginRequest
import com.optic.ecommerceappmvvm.domain.model.User
import com.optic.ecommerceappmvvm.domain.model.auth.LoginPlessRequest
import com.optic.ecommerceappmvvm.domain.model.auth.LoginSendCodeRequest
import com.optic.ecommerceappmvvm.domain.model.auth.LoginSendCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body() user: User,
    ): Response<AuthResponse>

    //Login sin password

    @POST("auth/code/request")
    suspend fun loginSendCode(
        @Body loginSendCode: LoginSendCodeRequest
    ): Response<LoginSendCodeResponse>

    @POST("auth/code/verify")
    suspend fun loginPless(
        @Body loginPlessRequest: LoginPlessRequest
    ): Response<AuthResponse>

}