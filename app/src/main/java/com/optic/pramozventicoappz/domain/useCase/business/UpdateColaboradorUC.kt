package com.optic.pramozventicoappz.domain.useCase.business


import com.optic.pramozventicoappz.domain.model.business.colaboradores.UserCollabUpdateRequest
import com.optic.pramozventicoappz.domain.repository.AuthRepository


class UpdateColaboradorUC(private val repository: AuthRepository) {

    suspend operator fun invoke(
        request: UserCollabUpdateRequest
    ) = repository.updateBusinessMember(
        request =  request
    )

}