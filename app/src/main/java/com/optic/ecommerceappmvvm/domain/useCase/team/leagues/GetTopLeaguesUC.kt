package com.optic.ecommerceappmvvm.domain.useCase.team.leagues

import com.optic.ecommerceappmvvm.domain.model.AuthResponse
import com.optic.ecommerceappmvvm.domain.repository.AuthRepository
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetTopLeaguesUC constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.getTopLeagues()
}