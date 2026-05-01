package com.optic.pramosreservasappz.domain.useCase.reservas.sales


import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetSalesByBusinessUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        businessId: Int,
        limit: Int
    ) = repository.getSalesByBusiness(
        businessId = businessId,
        limit = limit
    )
}