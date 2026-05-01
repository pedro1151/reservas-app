package com.optic.pramosreservasappz.domain.useCase.reservas.product


import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetProductsByBusinessUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        businessId: Int,
        name: String
    ) = repository.getProductByBusiness(
        businessId=businessId,
        name = name
    )
}