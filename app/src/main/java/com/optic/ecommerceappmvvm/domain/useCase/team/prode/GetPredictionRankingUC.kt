package com.optic.ecommerceappmvvm.domain.useCase.team.prode


import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionRequest
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetPredictionRankingUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(
        top: Int
    ) = repository.getPredictionRanking(top)
}