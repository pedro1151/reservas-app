package com.optic.pramosfootballappz.domain.useCase.auth

import com.optic.pramosfootballappz.domain.model.User
import com.optic.pramosfootballappz.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(user: User) =  repository.register(user);

}