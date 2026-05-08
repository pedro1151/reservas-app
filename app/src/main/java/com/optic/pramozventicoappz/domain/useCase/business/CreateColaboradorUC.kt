package com.optic.pramozventicoappz.domain.useCase.business

import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramozventicoappz.domain.repository.AuthRepository


class CreateColaboradorUC(private val repository: AuthRepository) {

    suspend operator fun invoke(
        user: UserCollabCreateRequest
    ) = repository.register_collab(user)

}