package com.optic.pramosfootballappz.data.dataSource.remote

import com.optic.pramosfootballappz.data.dataSource.remote.service.AuthService
import com.optic.pramosfootballappz.domain.model.AuthResponse
import com.optic.pramosfootballappz.domain.model.LoginRequest
import com.optic.pramosfootballappz.domain.model.User
import com.optic.pramosfootballappz.domain.model.auth.LoginPlessRequest
import com.optic.pramosfootballappz.domain.model.auth.LoginSendCodeRequest
import com.optic.pramosfootballappz.domain.model.auth.LoginSendCodeResponse
import retrofit2.Response

class AuthRemoteDataSourceImpl(private val authService: AuthService): AuthRemoteDataSource {

    override suspend fun login(email: String, password: String) = authService.login(LoginRequest( email, password))
    override suspend fun register(user: User): Response<AuthResponse> = authService.register(user)

    override suspend fun loginPless(
        email: String,
        code:String
    ): Response<AuthResponse> =authService.loginPless(LoginPlessRequest(email, code))

    override suspend fun loginSendCode(
        email: String
    ): Response<LoginSendCodeResponse> = authService.loginSendCode(LoginSendCodeRequest(email))

}