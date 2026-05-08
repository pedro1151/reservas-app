package com.optic.pramozventicoappz.domain.useCase.reservas.sales


import com.optic.pramozventicoappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleCreateRequest
import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class CreateSaleUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        request: SaleCreateRequest
    ) = repository.createSale(request)
}