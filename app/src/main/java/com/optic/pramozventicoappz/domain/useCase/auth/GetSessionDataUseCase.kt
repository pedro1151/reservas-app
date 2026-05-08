package com.optic.pramozventicoappz.domain.useCase.auth

import com.optic.pramozventicoappz.domain.repository.AuthRepository

class GetSessionDataUseCase constructor(private val repository: AuthRepository) {
    operator fun invoke() = repository.getSessionData()
}