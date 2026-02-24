package com.optic.pramosreservasappz.data.dataSource.remote

import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.model.auth.User
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramosreservasappz.domain.model.auth.RefreshTokenRequest
import retrofit2.Response

interface AuthRemoteDataSource {

    suspend fun login(email: String, password: String): Response<AuthResponse>
    suspend fun register(user: User): Response<AuthResponse>


    suspend fun loginPless(email: String, code:String): Response<AuthResponse>
    suspend fun loginSendCode(email: String): Response<LoginSendCodeResponse>

    // refresh token
    suspend fun refreshToken(
        request: RefreshTokenRequest
    ): Response<AuthResponse>

}