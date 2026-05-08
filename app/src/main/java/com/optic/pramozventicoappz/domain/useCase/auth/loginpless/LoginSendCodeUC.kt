package com.optic.pramozventicoappz.domain.useCase.auth.loginpless

import com.optic.pramozventicoappz.domain.repository.AuthRepository

class LoginSendCodeUC(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String) = repository.loginSendCode(email)

}