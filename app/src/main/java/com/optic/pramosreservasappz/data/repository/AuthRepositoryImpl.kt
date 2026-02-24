package com.optic.pramosreservasappz.data.repository

import com.optic.pramosreservasappz.data.dataSource.local.AuthLocalDataSource
import com.optic.pramosreservasappz.data.dataSource.remote.AuthRemoteDataSource
import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.model.auth.User
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramosreservasappz.domain.model.auth.RefreshTokenRequest
import com.optic.pramosreservasappz.domain.repository.AuthRepository
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.domain.util.ResponseToRequest
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
): AuthRepository {

    override suspend fun login(email: String, password: String): Resource<AuthResponse> = ResponseToRequest.send(
        authRemoteDataSource.login(email, password)
    )

    override suspend fun register(user: User): Resource<AuthResponse> = ResponseToRequest.send(
        authRemoteDataSource.register(user)
    )

    override suspend fun saveSession(authResponse: AuthResponse) = authLocalDataSource.saveSession(authResponse)

    //override suspend fun updateSession(user: User) = authLocalDataSource.updateSession(user)

    override suspend fun logout() = authLocalDataSource.logout()

    override fun getSessionData(): Flow<AuthResponse> = authLocalDataSource.getSessionData()

    override fun isUserLoggedIn(): Flow<Boolean> {
        // Si tu dataSource ya tiene el método
        return authLocalDataSource.isUserLoggedIn()
    }

    override suspend fun loginPless(email: String, code:String): Resource<AuthResponse> = ResponseToRequest.send(
        authRemoteDataSource.loginPless(email, code)
    )


    override suspend fun loginSendCode(email: String): Resource<LoginSendCodeResponse> = ResponseToRequest.send(
        authRemoteDataSource.loginSendCode(email)
    )


    override suspend fun refreshToken(
        request: RefreshTokenRequest
    ): Resource<AuthResponse> = ResponseToRequest.send(
        authRemoteDataSource.refreshToken(request)
    )



}