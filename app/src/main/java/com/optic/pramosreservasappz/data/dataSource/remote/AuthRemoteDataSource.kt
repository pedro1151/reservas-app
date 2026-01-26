package com.optic.pramosreservasappz.data.dataSource.remote

import com.optic.pramosreservasappz.domain.model.AuthResponse
import com.optic.pramosreservasappz.domain.model.User
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeResponse
import retrofit2.Response

interface AuthRemoteDataSource {

    suspend fun login(email: String, password: String): Response<AuthResponse>
    suspend fun register(user: User): Response<AuthResponse>


    suspend fun loginPless(email: String, code:String): Response<AuthResponse>
    suspend fun loginSendCode(email: String): Response<LoginSendCodeResponse>


}