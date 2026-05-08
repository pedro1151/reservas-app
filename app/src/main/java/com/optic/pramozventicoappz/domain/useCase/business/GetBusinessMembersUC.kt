package com.optic.pramozventicoappz.domain.useCase.business

import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramozventicoappz.domain.repository.AuthRepository


class GetBusinessMembersUC(private val repository: AuthRepository) {

    suspend operator fun invoke(
        businessId: Int,
        email: String,
        username: String
    ) = repository.getBusinessMembers(
        businessId = businessId,
        email =  email,
        username =  username
    )

}