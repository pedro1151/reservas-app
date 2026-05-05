package com.optic.pramosreservasappz.domain.useCase.external.googleplaybilling

import com.optic.pramosreservasappz.domain.repository.ExternalRepository

data class GetUserPurchasesUC(private val repository: ExternalRepository) {

    suspend operator fun invoke(
        onlyActive:Boolean,
        limit: Int,
        offset:Int
    ) = repository.getUserPurchases(
        onlyActive = onlyActive,
        limit = limit,
        offset = offset
    )


}