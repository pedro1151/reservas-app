package com.optic.pramosreservasappz.domain.useCase.reservas.saleitem



import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetItemsBySaleUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(
        saleId: Int
    ) = repository.getItemsBySale(saleId)
}