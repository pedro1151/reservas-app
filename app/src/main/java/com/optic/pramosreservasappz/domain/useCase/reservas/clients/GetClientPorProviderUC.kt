package com.optic.pramosreservasappz.domain.useCase.reservas.clients


import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetClientPorOwnerUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        ownerId:Int,
        fullName: String,
        email:String
    ) = repository.getClientsByOwner(
        ownerId = ownerId,
        fullName = fullName,
        email = email
    )
}