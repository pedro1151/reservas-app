package com.optic.pramosreservasappz.domain.useCase.reservas.saleitem


import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.saleitem.SaleItemUpdateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleItemCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleUpdateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class UpdateSaleItemUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        itemId:Int,
        request: SaleItemUpdateRequest
    ) = repository.updateSaleItem(
        request= request,
        itemId = itemId
    )
}