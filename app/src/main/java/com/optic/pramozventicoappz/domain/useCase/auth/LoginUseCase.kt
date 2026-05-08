package com.optic.pramozventicoappz.domain.useCase.auth

import com.optic.pramozventicoappz.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)

}