package com.optic.pramosreservasappz.domain.useCase.auth


import com.optic.pramosreservasappz.domain.useCase.auth.loginpless.LoginPlessUC
import com.optic.pramosreservasappz.domain.useCase.auth.loginpless.LoginSendCodeUC
import com.optic.pramosreservasappz.domain.useCase.business.CreateColaboradorUC
import com.optic.pramosreservasappz.domain.useCase.business.GetBusinessByIdUC
import com.optic.pramosreservasappz.domain.useCase.business.GetBusinessMembersUC

data class AuthUseCase(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val saveSession: SaveSessionUseCase,
    val getSessionData: GetSessionDataUseCase,
    val logout: LogoutUseCase,
    val loginPlessUC: LoginPlessUC,
    val loginSendCodeUC: LoginSendCodeUC,

    //busines

    val createColaboradorUC: CreateColaboradorUC,
    val getBusinessMembersUC: GetBusinessMembersUC,
    val getBusinessByIdUC: GetBusinessByIdUC
)
