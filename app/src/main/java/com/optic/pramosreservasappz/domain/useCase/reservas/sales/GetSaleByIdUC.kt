package com.optic.pramosreservasappz.domain.useCase.reservas.sales


import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetSaleByIdUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        saleId: Int
    ) = repository.getSaleById(saleId)
}