package com.optic.ecommerceappmvvm.data.repository

import com.optic.ecommerceappmvvm.data.dataSource.local.AuthLocalDataSource
import com.optic.ecommerceappmvvm.data.dataSource.remote.AuthRemoteDataSource
import com.optic.ecommerceappmvvm.domain.model.AuthResponse
import com.optic.ecommerceappmvvm.domain.model.User
import com.optic.ecommerceappmvvm.domain.model.auth.LoginSendCodeResponse
import com.optic.ecommerceappmvvm.domain.repository.AuthRepository
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.domain.util.ResponseToRequest
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


}