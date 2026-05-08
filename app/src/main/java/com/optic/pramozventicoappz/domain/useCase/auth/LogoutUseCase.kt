package com.optic.pramozventicoappz.domain.useCase.auth

import com.optic.pramozventicoappz.domain.repository.AuthRepository

class LogoutUseCase constructor(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}