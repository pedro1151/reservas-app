package com.optic.pramozventicoappz.domain.useCase.auth


import com.optic.pramozventicoappz.domain.useCase.auth.loginpless.LoginPlessUC
import com.optic.pramozventicoappz.domain.useCase.auth.loginpless.LoginSendCodeUC
import com.optic.pramozventicoappz.domain.useCase.business.CreateColaboradorUC
import com.optic.pramozventicoappz.domain.useCase.business.GetBusinessByIdUC
import com.optic.pramozventicoappz.domain.useCase.business.GetBusinessMembersUC
import com.optic.pramozventicoappz.domain.useCase.business.GetMemberUC
import com.optic.pramozventicoappz.domain.useCase.business.UpdateColaboradorUC

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
    val getBusinessByIdUC: GetBusinessByIdUC,
    val updateColaboradorUC: UpdateColaboradorUC,
    val getMemberUC: GetMemberUC
)
