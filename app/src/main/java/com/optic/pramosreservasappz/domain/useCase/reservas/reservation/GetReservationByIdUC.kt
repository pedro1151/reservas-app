package com.optic.pramosreservasappz.domain.useCase.reservas.reservation


import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationUpdateRequest
import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetReservationByIdUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        reservationId:Int
    ) = repository.getReservationById(
        reservationId =  reservationId
    )
}