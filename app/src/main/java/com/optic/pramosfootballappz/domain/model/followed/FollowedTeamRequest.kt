package com.optic.pramosfootballappz.domain.model.followed

import com.google.gson.Gson

data class FollowedTeamRequest(
    val team_id: Int? = null
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(data: String): FollowedPlayerRequest = Gson().fromJson(data, FollowedPlayerRequest::class.java)
    }
}

