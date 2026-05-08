package com.optic.pramozventicoappz.domain.useCase.reservas.saleitem


import com.optic.pramozventicoappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramozventicoappz.domain.model.saleitem.SaleItemUpdateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleCreateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleItemCreateRequest
import com.optic.pramozventicoappz.domain.model.sales.SaleUpdateRequest
import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class UpdateSaleItemUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        itemId:Int,
        request: SaleItemUpdateRequest
    ) = repository.updateSaleItem(
        request= request,
        itemId = itemId
    )
}