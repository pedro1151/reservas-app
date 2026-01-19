package com.optic.pramosfootballappz.domain.useCase.auth

import com.optic.pramosfootballappz.domain.repository.AuthRepository

class GetSessionDataUseCase constructor(private val repository: AuthRepository) {
    operator fun invoke() = repository.getSessionData()
}