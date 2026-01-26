package com.optic.pramosreservasappz.domain.useCase.auth

import com.optic.pramosreservasappz.domain.repository.AuthRepository

class GetSessionDataUseCase constructor(private val repository: AuthRepository) {
    operator fun invoke() = repository.getSessionData()
}