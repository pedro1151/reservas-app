package com.optic.pramozventicoappz.domain.useCase.business

import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserCollabCreateRequest
import com.optic.pramozventicoappz.domain.repository.AuthRepository


class GetBusinessByIdUC(private val repository: AuthRepository) {

    suspend operator fun invoke(
        businessId:Int,
        userId:Int
    ) = repository.getBusinessById(
        businessId = businessId,
        userId = userId
    )

}