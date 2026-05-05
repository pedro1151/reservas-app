package com.optic.pramosreservasappz.domain.useCase.external.googleplaybilling

import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.GooglePlayVerifyRequest
import com.optic.pramosreservasappz.domain.repository.ExternalRepository

data class GetUserEntitlementsUC(private val repository: ExternalRepository) {

    suspend operator fun invoke(
        onlyActive:Boolean
    ) = repository.getUserEntitlements(onlyActive)

}