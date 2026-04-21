package com.optic.pramosreservasappz.data.dataSource.remote

import com.optic.pramosreservasappz.data.dataSource.remote.service.AuthService
import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.model.auth.BasicUserResponse
import com.optic.pramosreservasappz.domain.model.auth.LoginRequest
import com.optic.pramosreservasappz.domain.model.auth.User
import com.optic.pramosreservasappz.domain.model.auth.LoginPlessRequest
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeRequest
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramosreservasappz.domain.model.auth.RefreshTokenRequest
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabUpdateRequest
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import retrofit2.Response

class AuthRemoteDataSourceImpl(private val authService: AuthService): AuthRemoteDataSource {

    override suspend fun login(email: String, password: String) = authService.login(LoginRequest( email, password))
    override suspend fun register(user: User): Response<AuthResponse> = authService.register(user)

    override suspend fun register_collab(
        user: UserCollabCreateRequest
    ): Response<BasicUserResponse> = authService.register_collab(user = user)



    override suspend fun loginPless(
        email: String,
        code:String
    ): Response<AuthResponse> =authService.loginPless(LoginPlessRequest(email, code))

    override suspend fun loginSendCode(
        email: String
    ): Response<LoginSendCodeResponse> = authService.loginSendCode(LoginSendCodeRequest(email))

    override suspend fun refreshToken(
        request: RefreshTokenRequest
    ): Response<AuthResponse> = authService.refresToken(request)


    // business

    override suspend fun getBusinessMembers(
        businessId: Int,
        email: String,
        username: String
    ): Response<List<UserMemberResponse>> = authService.getBusinessMembers(
        businessId =  businessId,
        email = email,
        username =  username
    )

    override suspend fun getBusinessById(
        businessId: Int,
        userId: Int
    ): Response<BusinessCompleteResponse> = authService.getBusinessById(
        businessId=businessId,
        userId = userId
    )

    override suspend fun updateBusinessMember(
        request: UserCollabUpdateRequest
    ): Response<DefaultResponse> = authService.updateBusinessMember(
        request = request
    )

    override suspend fun getUser(
        userId: Int
    ): Response<UserMemberResponse> = authService.getUser(userId)

}