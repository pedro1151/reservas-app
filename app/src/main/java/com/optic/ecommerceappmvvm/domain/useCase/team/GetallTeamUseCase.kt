package com.optic.ecommerceappmvvm.domain.useCase.team

import com.optic.ecommerceappmvvm.domain.model.AuthResponse
import com.optic.ecommerceappmvvm.domain.model.administracion.Country
import com.optic.ecommerceappmvvm.domain.repository.AuthRepository
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetallTeamUseCase constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(name:String, country: String, page:Int, size: Int) = repository.getAll(name, country, page, size)
}