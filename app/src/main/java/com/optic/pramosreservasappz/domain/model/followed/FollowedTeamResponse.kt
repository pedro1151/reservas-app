package com.optic.pramosreservasappz.domain.model.followed

data class FollowedTeamResponse(
    val id: Int,
    val team_id: Int,
    val user_id: Int,
    val created: String,
    val created_by: String
)