package com.optic.pramosfootballappz.domain.useCase.auth

import com.optic.pramosfootballappz.domain.model.AuthResponse
import com.optic.pramosfootballappz.domain.repository.AuthRepository

class SaveSessionUseCase constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(authResponse: AuthResponse) = repository.saveSession(authResponse)
}