package com.optic.pramozventicoappz.domain.useCase.reservas.sales


import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetSalesByBusinessUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        businessId: Int,
        limit: Int
    ) = repository.getSalesByBusiness(
        businessId = businessId,
        limit = limit
    )
}