package com.optic.pramosreservasappz.data.dataSource.remote

import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.model.auth.BasicUserResponse
import com.optic.pramosreservasappz.domain.model.auth.User
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramosreservasappz.domain.model.auth.RefreshTokenRequest
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
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


    // business
    suspend fun register_collab(
        user: UserCollabCreateRequest
    ): Response<BasicUserResponse>

    suspend fun getBusinessMembers(
        businessId: Int,
        email: String,
        username:String
    ): Response<List<UserMemberResponse>>


    suspend fun getBusinessById(
        businessId: Int,
        userId: Int
    ): Response<BusinessCompleteResponse>

}