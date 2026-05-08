package com.optic.pramozventicoappz.domain.useCase.reservas.sales


import com.optic.pramozventicoappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleCreateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleUpdateRequest
import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class UpdateSaleUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        saleId: Int,
        request: SaleUpdateRequest
    ) = repository.updateSale(
        saleId = saleId,
        request = request
    )
}