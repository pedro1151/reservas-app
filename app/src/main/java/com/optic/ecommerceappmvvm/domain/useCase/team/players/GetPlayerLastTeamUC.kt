package com.optic.ecommerceappmvvm.domain.useCase.team.players


import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetPlayerLastTeamUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(playerId: Int) = repository.getPlayerLastTeam(playerId)
}