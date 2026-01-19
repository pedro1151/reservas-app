package com.optic.pramosfootballappz.domain.useCase.auth

import com.optic.pramosfootballappz.domain.repository.AuthRepository

class LogoutUseCase constructor(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}