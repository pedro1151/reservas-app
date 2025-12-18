package com.optic.ecommerceappmvvm.domain.useCase.team.followedLeagues


import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class CreateFollowedLeagueUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(leagueId: Int, isAuthenticated: Boolean) = repository.createFollowedLeague(leagueId, isAuthenticated)
}