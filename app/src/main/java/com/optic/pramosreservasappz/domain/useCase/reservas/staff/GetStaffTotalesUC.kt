package com.optic.pramosreservasappz.domain.useCase.reservas.staff


import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetStaffTotalesUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke() = repository.getStaffTotales()
}