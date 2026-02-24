package com.optic.pramosreservasappz.domain.useCase.reservas.clients

import com.optic.pramosreservasappz.domain.model.clients.ClientCreateRequest
import com.optic.pramosreservasappz.domain.repository.AuthRepository
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class DeleteClientUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        clientId:Int
    ) = repository.deleteClient(clientId )
}