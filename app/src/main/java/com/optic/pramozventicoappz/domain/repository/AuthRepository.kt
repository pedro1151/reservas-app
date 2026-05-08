package com.optic.pramozventicoappz.domain.repository

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
import com.optic.pramozventicoappz.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

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


    //  business
    suspend fun register_collab(
        user: UserCollabCreateRequest
    ): Resource<BasicUserResponse>

    suspend fun getBusinessMembers(
        businessId: Int,
        email: String,
        username:String
    ):  Flow<Resource<List<UserMemberResponse>>>

    suspend fun getBusinessById(
        businessId: Int,
        userId: Int
    ): Resource<BusinessCompleteResponse>

    suspend fun updateBusinessMember(
        request: UserCollabUpdateRequest
    ): Resource<DefaultResponse>

    suspend fun getUser(
        userId: Int
    ):  Resource<UserMemberResponse>
}