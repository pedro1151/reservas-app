package com.optic.pramosreservasappz.domain.useCase.reservas.clients

import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetClientPorIdUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        clientId:Int
    ) = repository.getClientById(clientId)
}