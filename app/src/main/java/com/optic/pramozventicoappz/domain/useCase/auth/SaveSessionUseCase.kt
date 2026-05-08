package com.optic.pramozventicoappz.domain.useCase.auth

import com.optic.pramozventicoappz.domain.model.auth.AuthResponse
import com.optic.pramozventicoappz.domain.repository.AuthRepository

class SaveSessionUseCase constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(authResponse: AuthResponse) = repository.saveSession(authResponse)
}