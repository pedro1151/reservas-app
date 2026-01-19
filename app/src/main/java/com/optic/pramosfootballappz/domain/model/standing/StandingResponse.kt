package com.optic.pramosfootballappz.domain.model.standing

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StandingResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("league_id") val leagueId: Int,
    @SerializedName("team") val team: Team,
    @SerializedName("season") val season: Int,

    @SerializedName("rank") val rank: Int,
    @SerializedName("points") val points: Int,
    @SerializedName("goals_diff") val goalsDiff: Int? = null,

    @SerializedName("group") val group: String? = null,
    @SerializedName("form") val form: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("description") val description: String? = null,

    // ----- Campos "all"
    @SerializedName("all_played") val allPlayed: Int? = null,
    @SerializedName("all_win") val allWin: Int? = null,
    @SerializedName("all_draw") val allDraw: Int? = null,
    @SerializedName("all_lose") val allLose: Int? = null,
    @SerializedName("all_goals_for") val allGoalsFor: Int? = null,
    @SerializedName("all_goals_against") val allGoalsAgainst: Int? = null,

    // ----- Campos "home"
    @SerializedName("home_played") val homePlayed: Int? = null,
    @SerializedName("home_win") val homeWin: Int? = null,
    @SerializedName("home_draw") val homeDraw: Int? = null,
    @SerializedName("home_lose") val homeLose: Int? = null,
    @SerializedName("home_goals_for") val homeGoalsFor: Int? = null,
    @SerializedName("home_goals_against") val homeGoalsAgainst: Int? = null,

    // ----- Campos "away"
    @SerializedName("away_played") val awayPlayed: Int? = null,
    @SerializedName("away_win") val awayWin: Int? = null,
    @SerializedName("away_draw") val awayDraw: Int? = null,
    @SerializedName("away_lose") val awayLose: Int? = null,
    @SerializedName("away_goals_for") val awayGoalsFor: Int? = null,
    @SerializedName("away_goals_against") val awayGoalsAgainst: Int? = null,

    // Última actualización
    @SerializedName("update") val update: String? = null
) : Serializable {

    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(data: String): StandingResponse = Gson().fromJson(data, StandingResponse::class.java)
    }

    data class Team(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("logo") val logo: String
    )
}