package com.optic.pramosreservasappz.domain.useCase.reservas.services


import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceUpdateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class UpdateServiceUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        serviceId: Int,
        request: ServiceUpdateRequest
    ) = repository.updateService(serviceId, request)
}