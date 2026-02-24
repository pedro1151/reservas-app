package com.optic.pramosreservasappz.domain.useCase.reservas.services


import com.optic.pramosreservasappz.domain.model.services.ServiceCreateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class CreateServiceUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(request: ServiceCreateRequest) = repository.createService(request)
}