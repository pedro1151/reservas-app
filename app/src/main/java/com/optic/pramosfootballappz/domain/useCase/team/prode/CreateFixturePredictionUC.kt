package com.optic.pramosfootballappz.domain.useCase.team.prode


import com.optic.pramosfootballappz.domain.model.prode.FixturePredictionRequest
import com.optic.pramosfootballappz.domain.repository.TeamRepository

class CreateFixturePredictionUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(
        request: FixturePredictionRequest,
        isAuthenticated: Boolean
    ) = repository.createFixturePrediction(request, isAuthenticated)
}