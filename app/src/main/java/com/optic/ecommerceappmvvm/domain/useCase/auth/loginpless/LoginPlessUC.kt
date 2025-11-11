package com.optic.ecommerceappmvvm.domain.useCase.auth.loginpless

import com.optic.ecommerceappmvvm.domain.repository.AuthRepository

class LoginPlessUC(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, code:String) = repository.loginPless(email, code)

}