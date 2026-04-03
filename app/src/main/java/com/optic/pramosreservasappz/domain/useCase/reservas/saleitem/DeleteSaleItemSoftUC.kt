package com.optic.pramosreservasappz.domain.useCase.reservas.saleitem


import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class DeleteSaleItemSoftUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        itemId:Int,
    ) = repository.deleteSaleSoft(
        itemId
    )
}