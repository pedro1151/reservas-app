package com.optic.ecommerceappmvvm.domain.useCase.team.players


import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetAllPlayersUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.getallPlayers()
}