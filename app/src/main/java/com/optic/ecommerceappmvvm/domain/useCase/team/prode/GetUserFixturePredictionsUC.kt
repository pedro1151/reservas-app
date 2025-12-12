package com.optic.ecommerceappmvvm.domain.useCase.team.prode


import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionRequest
import com.optic.ecommerceappmvvm.domain.repository.TeamRepository

class GetUserFixturePredictionsUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(leagueId: Int, season:Int) = repository.getUserFixturePredictions(leagueId, season)
}