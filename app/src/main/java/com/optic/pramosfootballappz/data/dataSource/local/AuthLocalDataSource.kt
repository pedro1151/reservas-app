package com.optic.pramosfootballappz.data.dataSource.local

import com.optic.pramosfootballappz.domain.model.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {

    suspend fun saveSession(authResponse: AuthResponse)
    //suspend fun updateSession(user: User)
    suspend fun logout()
    fun getSessionData(): Flow<AuthResponse>

    fun isUserLoggedIn(): Flow<Boolean>

}