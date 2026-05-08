package com.optic.pramozventicoappz.data.dataSource.local

import com.optic.pramozventicoappz.domain.model.auth.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {

    suspend fun saveSession(authResponse: AuthResponse)
    //suspend fun updateSession(user: User)
    suspend fun logout()
    fun getSessionData(): Flow<AuthResponse>

    fun isUserLoggedIn(): Flow<Boolean>

}