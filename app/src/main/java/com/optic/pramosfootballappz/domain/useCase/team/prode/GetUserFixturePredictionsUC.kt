package com.optic.pramosfootballappz.domain.useCase.team.prode


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetUserFixturePredictionsUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(leagueId: Int, season:Int) = repository.getUserFixturePredictions(leagueId, season)
}