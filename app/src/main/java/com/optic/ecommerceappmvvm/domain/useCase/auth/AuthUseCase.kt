package com.optic.ecommerceappmvvm.domain.useCase.auth


import com.optic.ecommerceappmvvm.domain.model.auth.LoginSendCodeResponse
import com.optic.ecommerceappmvvm.domain.useCase.auth.loginpless.LoginPlessUC
import com.optic.ecommerceappmvvm.domain.useCase.auth.loginpless.LoginSendCodeUC

data class AuthUseCase(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val saveSession: SaveSessionUseCase,
    val getSessionData: GetSessionDataUseCase,
    val logout: LogoutUseCase,
    val loginPlessUC: LoginPlessUC,
    val loginSendCodeUC: LoginSendCodeUC,
)
