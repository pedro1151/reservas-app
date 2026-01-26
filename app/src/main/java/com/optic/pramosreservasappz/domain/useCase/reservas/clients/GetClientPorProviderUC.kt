package com.optic.pramosreservasappz.domain.useCase.reservas.clients

import com.optic.pramosreservasappz.domain.repository.AuthRepository
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetClientPorProviderUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        providerId:Int,
        fullName: String,
        email:String
    ) = repository.getClientsByProvider(providerId, fullName, email)
}