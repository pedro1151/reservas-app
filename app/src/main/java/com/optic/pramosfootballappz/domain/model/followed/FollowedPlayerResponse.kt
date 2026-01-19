package com.optic.pramosfootballappz.domain.model.followed

data class FollowedPlayerResponse(
    val id: Int,
    val player_id: Int,
    val user_id: Int,
    val created: String,
    val created_by: String
)