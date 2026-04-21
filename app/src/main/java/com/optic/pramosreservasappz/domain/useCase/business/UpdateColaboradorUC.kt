package com.optic.pramosreservasappz.domain.useCase.business


import com.optic.pramosreservasappz.domain.model.business.colaboradores.UserCollabUpdateRequest
import com.optic.pramosreservasappz.domain.repository.AuthRepository


class UpdateColaboradorUC(private val repository: AuthRepository) {

    suspend operator fun invoke(
        request: UserCollabUpdateRequest
    ) = repository.updateBusinessMember(
        request =  request
    )

}