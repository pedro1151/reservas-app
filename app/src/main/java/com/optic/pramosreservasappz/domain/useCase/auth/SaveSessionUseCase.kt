package com.optic.pramosreservasappz.domain.useCase.auth

import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.repository.AuthRepository

class SaveSessionUseCase constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(authResponse: AuthResponse) = repository.saveSession(authResponse)
}