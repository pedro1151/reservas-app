package com.optic.pramosreservasappz.domain.useCase.auth.loginpless

import com.optic.pramosreservasappz.domain.repository.AuthRepository

class LoginPlessUC(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, code:String) = repository.loginPless(email, code)

}