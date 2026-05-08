package com.optic.pramozventicoappz.domain.useCase.reservas.saleitem


import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class DeleteSaleItemSoftUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        itemId:Int,
    ) = repository.deleteSaleSoft(
        itemId
    )
}