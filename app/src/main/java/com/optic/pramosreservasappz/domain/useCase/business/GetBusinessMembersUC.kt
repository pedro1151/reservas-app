package com.optic.pramosreservasappz.domain.useCase.business

import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.repository.AuthRepository


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