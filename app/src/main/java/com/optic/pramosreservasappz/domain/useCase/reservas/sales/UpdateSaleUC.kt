package com.optic.pramosreservasappz.domain.useCase.reservas.sales


import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleUpdateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class UpdateSaleUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        saleId: Int,
        request: SaleUpdateRequest
    ) = repository.updateSale(
        saleId = saleId,
        request = request
    )
}