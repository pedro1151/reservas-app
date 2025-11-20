package com.optic.ecommerceappmvvm.domain.useCase.team.equipos

import com.optic.ecommerceappmvvm.domain.model.AuthResponse
import com.optic.ecommerceappmvvm.domain.repository.AuthRepository
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetTeamStatsUC constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(season: Int, teamId: Int, date:String?=null) = repository.getTeamStats(season, teamId, date)
}