package com.optic.pramosreservasappz.domain.useCase.reservas.salestats


import com.optic.pramosreservasappz.domain.repository.ReservasRepository


class GetSaleStatsUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        businessId: Int,
        year: Int
    ) = repository.getSaleStats(
        businessId= businessId,
        year   = year
    )
}