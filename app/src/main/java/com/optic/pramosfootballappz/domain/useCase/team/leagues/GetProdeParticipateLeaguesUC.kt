package com.optic.pramosfootballappz.domain.useCase.team.leagues

import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetProdeParticipateLeaguesUC constructor(private val repository: TeamRepository) {
    suspend operator fun invoke(userId:Int) = repository.getProdeParticipateLeagues(userId)
}