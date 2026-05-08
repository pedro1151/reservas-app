package com.optic.pramozventicoappz.domain.useCase.reservas.saleitem


import com.optic.pramozventicoappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleCreateRequest
import com.optic.pramozventicoappz.domain.model.saleitem.SaleItemCreateRequest
import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class CreateSaleItemBulkUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        request: List<SaleItemCreateRequest>
    ) = repository.createSaleItemBulk(request)
}