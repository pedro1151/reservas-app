package com.optic.pramosreservasappz.domain.model.followed
import com.google.gson.Gson

data class FollowedLeagueRequest(
    val league_id: Int? = null
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(data: String): FollowedLeagueRequest = Gson().fromJson(data, FollowedLeagueRequest::class.java)
    }
}
