package com.optic.pramozventicoappz.domain.useCase.reservas.services


import com.optic.pramozventicoappz.domain.model.services.ServiceCreateRequest
import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class CreateServiceUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(request: ServiceCreateRequest) = repository.createService(request)
}