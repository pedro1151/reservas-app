package com.optic.pramozventicoappz.domain.useCase.business

import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramozventicoappz.domain.repository.AuthRepository


class GetMemberUC(private val repository: AuthRepository) {

    suspend operator fun invoke(
        userId:Int
    ) = repository.getUser(
        userId = userId
    )

}