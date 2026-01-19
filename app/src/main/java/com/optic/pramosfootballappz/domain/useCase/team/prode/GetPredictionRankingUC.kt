package com.optic.pramosfootballappz.domain.useCase.team.prode


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetPredictionRankingUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(
        top: Int
    ) = repository.getPredictionRanking(top)
}