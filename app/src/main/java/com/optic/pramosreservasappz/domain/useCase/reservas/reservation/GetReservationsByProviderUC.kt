package com.optic.pramosreservasappz.domain.useCase.reservas.reservation


import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetReservationsByProviderUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        providerId:Int
    ) = repository.getReservationsByProvider(providerId)
}