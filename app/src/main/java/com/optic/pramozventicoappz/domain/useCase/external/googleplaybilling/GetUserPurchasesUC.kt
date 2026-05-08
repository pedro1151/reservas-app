package com.optic.pramozventicoappz.domain.useCase.external.googleplaybilling

import com.optic.pramozventicoappz.domain.repository.ExternalRepository

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