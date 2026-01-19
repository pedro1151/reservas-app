package com.optic.pramosfootballappz.domain.useCase.team.prode


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetUserPredictionSummaryUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(userId: Int, season:Int) = repository.getUserPredictionSummaryEnriched(userId, season)
}