package com.optic.pramosreservasappz.domain.useCase.auth

import com.optic.pramosreservasappz.domain.model.User
import com.optic.pramosreservasappz.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(user: User) =  repository.register(user);

}