package com.optic.pramozventicoappz.domain.useCase.reservas.services


import com.optic.pramozventicoappz.domain.repository.ReservasRepository

class GetServicesPorProviderUC constructor(private val repository: ReservasRepository) {
    suspend operator fun invoke(providerId:Int, name:String) = repository.getServicesByProvider(providerId, name)
}