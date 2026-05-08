package com.optic.pramozventicoappz.domain.useCase.reservas.clients

import com.optic.pramozventicoappz.domain.model.clients.ClientCreateRequest
import com.optic.pramozventicoappz.domain.repository.AuthRepository
import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class DeleteClientUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        clientId:Int
    ) = repository.deleteClient(clientId )
}