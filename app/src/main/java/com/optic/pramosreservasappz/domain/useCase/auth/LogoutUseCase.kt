package com.optic.pramosreservasappz.domain.useCase.auth

import com.optic.pramosreservasappz.domain.repository.AuthRepository

class LogoutUseCase constructor(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}