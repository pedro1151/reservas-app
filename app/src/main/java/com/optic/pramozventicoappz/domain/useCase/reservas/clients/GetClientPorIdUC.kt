package com.optic.pramozventicoappz.domain.useCase.reservas.clients

import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetClientPorIdUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        clientId:Int
    ) = repository.getClientById(clientId)
}