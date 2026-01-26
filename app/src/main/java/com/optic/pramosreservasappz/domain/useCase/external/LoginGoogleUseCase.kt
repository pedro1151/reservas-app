package com.optic.pramosreservasappz.domain.useCase.external

import com.optic.pramosreservasappz.domain.repository.ExternalRepository

class LoginGoogleUseCase(private val repository: ExternalRepository) {

    suspend operator fun invoke(id_token: String) = repository.login(id_token)

}