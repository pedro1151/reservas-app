package com.optic.pramosreservasappz.domain.useCase.external.googleplaybilling

import com.optic.pramosreservasappz.domain.model.external.googleplaybilling.GooglePlayVerifyRequest
import com.optic.pramosreservasappz.domain.repository.ExternalRepository

data class GooglePlayVerifyPurchasesUC(private val repository: ExternalRepository) {

    suspend operator fun invoke(
        request: GooglePlayVerifyRequest
    ) = repository.googlePlayVerifyPurchase(
        request
    )

}