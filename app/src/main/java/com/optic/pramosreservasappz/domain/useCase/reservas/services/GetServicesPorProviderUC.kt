package com.optic.pramosreservasappz.domain.useCase.reservas.services


import com.optic.pramosreservasappz.domain.repository.ReservasRepository

class GetServicesPorProviderUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(providerId:Int, name:String) = repository.getServicesByProvider(providerId, name)
}