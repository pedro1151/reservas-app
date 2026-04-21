package com.optic.pramosreservasappz.domain.useCase.business

import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.repository.AuthRepository


class GetMemberUC(private val repository: AuthRepository) {

    suspend operator fun invoke(
        userId:Int
    ) = repository.getUser(
        userId = userId
    )

}