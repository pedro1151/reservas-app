package com.optic.pramosfootballappz.domain.useCase.team.equipos

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetTeamStatsUC constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(season: Int, teamId: Int, date:String?=null) = repository.getTeamStats(season, teamId, date)
}