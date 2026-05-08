package com.optic.pramozventicoappz.domain.useCase.reservas.reservation


import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetReservationsByProviderUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        providerId:Int
    ) = repository.getReservationsByProvider(providerId)
}