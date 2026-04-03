package com.optic.pramosreservasappz.domain.useCase.reservas.saleitem



import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetSaleItemByIdUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        itemId: Int
    ) = repository.getSaleItemById(itemId)
}