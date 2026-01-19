package com.optic.pramosfootballappz.domain.useCase.external

import com.optic.pramosfootballappz.domain.repository.ExternalRepository

class LoginGoogleUseCase(private val repository: ExternalRepository) {

    suspend operator fun invoke(id_token: String) = repository.login(id_token)

}