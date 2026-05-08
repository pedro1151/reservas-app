package com.optic.pramozventicoappz.domain.useCase.reservas.reservation


import com.optic.pramozventicoappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramozventicoappz.domain.model.reservations.ReservationUpdateRequest
import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetReservationsUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
    ) = repository.getReservations()
}