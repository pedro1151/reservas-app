package com.optic.pramozventicoappz.domain.useCase.reservas.clients


import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetClientPorBusinessUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        businessId:Int,
        fullName: String,
        email:String
    ) = repository.getClientsByBusiness(
        businessId = businessId,
        fullName = fullName,
        email = email
    )
}