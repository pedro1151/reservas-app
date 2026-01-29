package com.optic.pramosreservasappz.domain.useCase.reservas.services


import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetServicesPorIdUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        serviceId:Int
    ) = repository.getServiceById(serviceId)
}