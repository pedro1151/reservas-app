package com.optic.pramozventicoappz.domain.useCase.external.googleplaybilling

import com.optic.pramozventicoappz.domain.model.external.googleplaybilling.GooglePlayVerifyRequest
import com.optic.pramozventicoappz.domain.repository.ExternalRepository

data class GetUserEntitlementsUC(private val repository: ExternalRepository) {

    suspend operator fun invoke(
        onlyActive:Boolean
    ) = repository.getUserEntitlements(onlyActive)

}