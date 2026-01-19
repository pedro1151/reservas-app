package com.optic.pramosfootballappz.domain.useCase.auth.loginpless

import com.optic.pramosfootballappz.domain.repository.AuthRepository

class LoginSendCodeUC(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String) = repository.loginSendCode(email)

}