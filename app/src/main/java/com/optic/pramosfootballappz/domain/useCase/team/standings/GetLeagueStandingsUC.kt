package com.optic.pramosfootballappz.domain.useCase.team.standings


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetLeagueStandingsUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(leagueId: Int, season:Int) = repository.getLeagueStandings(leagueId, season)
}