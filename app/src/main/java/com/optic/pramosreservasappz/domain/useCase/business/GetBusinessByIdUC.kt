package com.optic.pramosreservasappz.domain.useCase.business

import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramosreservasappz.domain.repository.AuthRepository


class GetBusinessByIdUC(private val repository: AuthRepository) {

    suspend operator fun invoke(
        businessId:Int,
        userId:Int
    ) = repository.getBusinessById(
        businessId = businessId,
        userId = userId
    )

}