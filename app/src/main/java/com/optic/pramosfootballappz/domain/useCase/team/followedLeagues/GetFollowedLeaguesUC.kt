package com.optic.pramosfootballappz.domain.useCase.team.followedLeagues


import com.optic.pramosfootballappz.domain.repository.TeamRepository

class GetFollowedLeaguesUC  constructor(private val repository: TeamRepository) {
    suspend operator fun invoke() = repository.getFollowedLeagues()
}