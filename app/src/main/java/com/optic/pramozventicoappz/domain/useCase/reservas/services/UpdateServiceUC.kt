package com.optic.pramozventicoappz.domain.useCase.reservas.services


import com.optic.pramozventicoappz.domain.model.services.ServiceCreateRequest
import com.optic.pramozventicoappz.domain.model.services.ServiceUpdateRequest
import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class UpdateServiceUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        serviceId: Int,
        request: ServiceUpdateRequest
    ) = repository.updateService(serviceId, request)
}