package com.optic.pramosreservasappz.domain.useCase.reservas.clients

import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.repository.AuthRepository
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class CreateClientUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        request: ClientCreateRequest
    ) = repository.createClient(request)
}