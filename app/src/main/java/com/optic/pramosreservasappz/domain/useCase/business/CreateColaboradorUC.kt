package com.optic.pramosreservasappz.domain.useCase.business

import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.repository.AuthRepository


class CreateColaboradorUC(private val repository: AuthRepository) {

    suspend operator fun invoke(
        user: UserCollabCreateRequest
    ) = repository.register_collab(user)

}