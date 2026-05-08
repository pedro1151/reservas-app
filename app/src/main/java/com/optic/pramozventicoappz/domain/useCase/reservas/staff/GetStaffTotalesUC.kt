package com.optic.pramozventicoappz.domain.useCase.reservas.staff


import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetStaffTotalesUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke() = repository.getStaffTotales()
}