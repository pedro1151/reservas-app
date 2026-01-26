package com.optic.pramosreservasappz.domain.useCase.reservas.clients

import com.optic.pramosreservasappz.domain.repository.AuthRepository
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetClientPorProviderUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(providerId:Int) = repository.getClientsByProvider(providerId)
}