package com.optic.ecommerceappmvvm.domain.useCase.team

import com.optic.ecommerceappmvvm.domain.model.AuthResponse
import com.optic.ecommerceappmvvm.domain.repository.AuthRepository
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetPlayersUseCase constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(name: String?, page:Int, size: Int) = repository.getPlayers(name, page, size)
}