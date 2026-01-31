package com.optic.pramosreservasappz.domain.useCase.reservas.clients

import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientUpdateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class UpdateClientUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        clientId:Int,
        request: ClientUpdateRequest
    ) = repository.updateClient(clientId=clientId, request=request)
}