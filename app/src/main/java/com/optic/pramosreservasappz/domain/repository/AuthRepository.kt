package com.optic.pramosreservasappz.domain.repository

import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.model.auth.User
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramosreservasappz.domain.model.auth.RefreshTokenRequest
import com.optic.pramosreservasappz.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(email: String, password: String):  Resource<AuthResponse>
    suspend fun register(user: User):  Resource<AuthResponse>
    suspend fun saveSession(authResponse: AuthResponse)
    suspend fun logout()
    fun getSessionData(): Flow<AuthResponse>
    fun isUserLoggedIn(): Flow<Boolean>

    //password less login
    suspend fun loginPless(email: String, code:String):  Resource<AuthResponse>
    suspend fun loginSendCode(email: String):  Resource<LoginSendCodeResponse>

    // refresh token
    suspend fun refreshToken(
        request: RefreshTokenRequest
    ):  Resource<AuthResponse>
}