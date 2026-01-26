package com.optic.pramosreservasappz.domain.useCase.auth

import com.optic.pramosreservasappz.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)

}