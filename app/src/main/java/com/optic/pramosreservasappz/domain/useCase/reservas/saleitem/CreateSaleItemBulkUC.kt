package com.optic.pramosreservasappz.domain.useCase.reservas.saleitem


import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemCreateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class CreateSaleItemBulkUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        request: List<SaleItemCreateRequest>
    ) = repository.createSaleItemBulk(request)
}