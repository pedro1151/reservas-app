package com.optic.pramozventicoappz.data.dataSource.remote

import com.optic.pramozventicoappz.domain.model.auth.AuthResponse
import com.optic.pramozventicoappz.domain.model.auth.BasicUserResponse
import com.optic.pramozventicoappz.domain.model.auth.User
import com.optic.pramozventicoappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramozventicoappz.domain.model.auth.RefreshTokenRequest
import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserCollabUpdateRequest
import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramozventicoappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramozventicoappz.domain.model.response.DefaultResponse
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

    suspend fun updateBusinessMember(
        request: UserCollabUpdateRequest
    ): Response<DefaultResponse>

    suspend fun getUser(
        userId: Int
    ): Response<UserMemberResponse>

}