package com.optic.pramosfootballappz.domain.useCase.auth

import com.optic.pramosfootballappz.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)

}