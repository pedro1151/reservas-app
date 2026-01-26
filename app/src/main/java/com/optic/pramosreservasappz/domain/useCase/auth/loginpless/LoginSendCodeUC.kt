package com.optic.pramosreservasappz.domain.useCase.auth.loginpless

import com.optic.pramosreservasappz.domain.repository.AuthRepository

class LoginSendCodeUC(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String) = repository.loginSendCode(email)

}