package com.optic.pramozventicoappz.domain.useCase.auth

import com.optic.pramozventicoappz.domain.model.auth.User
import com.optic.pramozventicoappz.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(user: User) =  repository.register(user);

}