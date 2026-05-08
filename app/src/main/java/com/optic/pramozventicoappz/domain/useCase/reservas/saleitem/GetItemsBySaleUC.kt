package com.optic.pramozventicoappz.domain.useCase.reservas.saleitem



import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetItemsBySaleUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        saleId: Int
    ) = repository.getItemsBySale(saleId)
}