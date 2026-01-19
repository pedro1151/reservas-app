package com.optic.pramosfootballappz.data.dataSource.remote

import com.optic.pramosfootballappz.domain.model.AuthResponse
import com.optic.pramosfootballappz.domain.model.User
import com.optic.pramosfootballappz.domain.model.auth.LoginSendCodeResponse
import retrofit2.Response

interface AuthRemoteDataSource {

    suspend fun login(email: String, password: String): Response<AuthResponse>
    suspend fun register(user: User): Response<AuthResponse>


    suspend fun loginPless(email: String, code:String): Response<AuthResponse>
    suspend fun loginSendCode(email: String): Response<LoginSendCodeResponse>


}