package com.optic.ecommerceappmvvm.domain.useCase.team.prode


import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionRequest
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetUserPredictionSummaryUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(userId: Int, season:Int) = repository.getUserPredictionSummaryEnriched(userId, season)
}