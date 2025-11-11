package com.optic.ecommerceappmvvm.data.dataSource.local

import com.optic.ecommerceappmvvm.data.dataSource.local.datastore.AuthDatastore
import com.optic.ecommerceappmvvm.data.dataSource.local.AuthLocalDataSource
import com.optic.ecommerceappmvvm.domain.model.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthLocalDataSourceImpl constructor(private val authDatastore: AuthDatastore):
    AuthLocalDataSource {

    override suspend fun saveSession(authResponse: AuthResponse) = authDatastore.save(authResponse)
  //  override suspend fun updateSession(user: User) = authDatastore.update(user)
    override suspend fun logout() = authDatastore.delete()
    override fun getSessionData(): Flow<AuthResponse> = authDatastore.getData()

    // ✅ Nuevo: flujo que indica si el usuario está logueado
    override fun isUserLoggedIn(): Flow<Boolean> {
        return getSessionData().map { auth ->
            !auth.token.isNullOrBlank()
        }
    }
}