package com.optic.pramozventicoappz.domain.useCase.reservas.product


import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetProductsByBusinessUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        businessId: Int,
        name: String
    ) = repository.getProductByBusiness(
        businessId=businessId,
        name = name
    )
}