package com.optic.pramozventicoappz.domain.useCase.reservas.reservation


import com.optic.pramozventicoappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class CreateReservationUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        request: ReservationCreateRequest
    ) = repository.createReservation(request)
}