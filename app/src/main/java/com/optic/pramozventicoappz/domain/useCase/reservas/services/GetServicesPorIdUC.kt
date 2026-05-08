package com.optic.pramozventicoappz.domain.useCase.reservas.services


import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetServicesPorIdUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        serviceId:Int
    ) = repository.getServiceById(serviceId)
}