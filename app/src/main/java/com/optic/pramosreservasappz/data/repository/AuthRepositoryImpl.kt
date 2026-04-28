package com.optic.pramosreservasappz.data.repository

import com.optic.pramosreservasappz.data.dataSource.local.AuthLocalDataSource
import com.optic.pramosreservasappz.data.dataSource.remote.AuthRemoteDataSource
import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.model.auth.BasicUserResponse
import com.optic.pramosreservasappz.domain.model.auth.User
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramosreservasappz.domain.model.auth.RefreshTokenRequest
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabUpdateRequest
import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserMemberResponse
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramosreservasappz.domain.model.response.DefaultResponse
import com.optic.pramosreservasappz.domain.repository.AuthRepository
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.domain.util.ResponseToRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
): AuthRepository {

    override suspend fun login(email: String, password: String): Resource<AuthResponse>
    = ResponseToRequest.send(
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


    override suspend fun refreshToken(
        request: RefreshTokenRequest
    ): Resource<AuthResponse> = ResponseToRequest.send(
        authRemoteDataSource.refreshToken(request)
    )


    // business

    override suspend fun register_collab(
        user: UserCollabCreateRequest
    ): Resource<BasicUserResponse> = ResponseToRequest.send(
        authRemoteDataSource.register_collab(user)
    )

    override suspend fun getBusinessMembers(
        businessId: Int,
        email: String,
        username: String
    ): Flow<Resource<List<UserMemberResponse>>> = flow {
        emit(
            ResponseToRequest.send(
                authRemoteDataSource.getBusinessMembers(
                    businessId =  businessId,
                    email =  email,
                    username = username

                )


            )
        )
    }

    override suspend fun getBusinessById(
        businessId: Int,
        userId: Int
    ): Resource<BusinessCompleteResponse> =
        ResponseToRequest.send(
        authRemoteDataSource.getBusinessById(
            businessId = businessId,
            userId = userId
        )
    )

    override suspend fun updateBusinessMember(
        request: UserCollabUpdateRequest
    ): Resource<DefaultResponse> =
        ResponseToRequest.send(
            authRemoteDataSource.updateBusinessMember(
            request = request
            )
        )

    override suspend fun getUser(
        userId: Int
    ): Resource<UserMemberResponse> =
        ResponseToRequest.send(
            authRemoteDataSource.getUser(
                userId = userId
            )
        )


}