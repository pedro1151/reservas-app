package com.optic.pramosreservasappz.domain.model.followed

data class FollowedLeagueResponse(
    val id: Int,
    val league_id: Int,
    val user_id: Int,
    val created: String,
    val created_by: String
)