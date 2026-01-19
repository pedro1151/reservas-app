package com.optic.pramosfootballappz.domain.useCase.team.followedLeagues


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class CreateFollowedLeagueUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(leagueId: Int, isAuthenticated: Boolean) = repository.createFollowedLeague(leagueId, isAuthenticated)
}