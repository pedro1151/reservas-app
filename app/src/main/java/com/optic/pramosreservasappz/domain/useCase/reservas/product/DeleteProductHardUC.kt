package com.optic.pramosreservasappz.domain.useCase.reservas.product


import com.optic.pramosreservasappz.domain.model.product.ProductCreateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.sales.SaleCreateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class DeleteProductHardUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        productId: Int
    ) = repository.deleteSaleHard(productId)
}