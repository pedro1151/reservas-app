package com.optic.pramosreservasappz.domain.useCase.reservas.reservation


import com.optic.pramosreservasappz.domain.model.reservas.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.reservas.reservations.ReservationUpdateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetReservationsUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
    ) = repository.getReservations()
}