package com.optic.pramosreservasappz.domain.useCase.auth


import com.optic.pramosreservasappz.domain.useCase.auth.loginpless.LoginPlessUC
import com.optic.pramosreservasappz.domain.useCase.auth.loginpless.LoginSendCodeUC

data class AuthUseCase(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val saveSession: SaveSessionUseCase,
    val getSessionData: GetSessionDataUseCase,
    val logout: LogoutUseCase,
    val loginPlessUC: LoginPlessUC,
    val loginSendCodeUC: LoginSendCodeUC,
)
