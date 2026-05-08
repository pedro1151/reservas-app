package com.optic.pramozventicoappz.domain.useCase.reservas.saleitem



import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetSaleItemByIdUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        itemId: Int
    ) = repository.getSaleItemById(itemId)
}