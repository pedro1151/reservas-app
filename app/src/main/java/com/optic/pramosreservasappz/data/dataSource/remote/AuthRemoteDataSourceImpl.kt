package com.optic.pramosreservasappz.data.dataSource.remote

import com.optic.pramosreservasappz.data.dataSource.remote.service.AuthService
import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.model.auth.LoginRequest
import com.optic.pramosreservasappz.domain.model.auth.User
import com.optic.pramosreservasappz.domain.model.auth.LoginPlessRequest
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeRequest
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramosreservasappz.domain.model.auth.RefreshTokenRequest
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

    override suspend fun refreshToken(
        request: RefreshTokenRequest
    ): Response<AuthResponse> = authService.refresToken(request)

}