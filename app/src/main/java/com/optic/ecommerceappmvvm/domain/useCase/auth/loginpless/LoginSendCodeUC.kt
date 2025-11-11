package com.optic.ecommerceappmvvm.domain.useCase.auth.loginpless

import com.optic.ecommerceappmvvm.domain.repository.AuthRepository

class LoginSendCodeUC(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String) = repository.loginSendCode(email)

}