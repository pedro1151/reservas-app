package com.optic.pramosfootballappz.domain.model.team
import com.google.gson.annotations.SerializedName

data class LeagueStatsSchema(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("logo") val logo: String?,
    @SerializedName("season") val season: Int?
)

data class TeamStatsTeamSchema(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("logo") val logo: String?
)

data class TeamStatisticsDataSchema(
    @SerializedName("league") val league: LeagueStatsSchema,
    @SerializedName("team") val team: TeamStatsTeamSchema,

    @SerializedName("form") val form: String? = null,

    @SerializedName("fixtures") val fixtures: Map<String, Any>? = null,
    @SerializedName("goals") val goals: Map<String, Any>? = null,
    @SerializedName("biggest") val biggest: Map<String, Any>? = null,
    @SerializedName("clean_sheet") val cleanSheet: Map<String, Any>? = null,
    @SerializedName("failed_to_score") val failedToScore: Map<String, Any>? = null,
    @SerializedName("penalty") val penalty: Map<String, Any>? = null,
    @SerializedName("lineups") val lineups: List<Map<String, Any>>? = null,
    @SerializedName("cards") val cards: Map<String, Any>? = null
)

data class TeamStatsResponse(
    @SerializedName("get") val get: String,
    @SerializedName("parameters") val parameters: Map<String, Any>,
    @SerializedName("errors") val errors: List<String>,
    @SerializedName("results") val results: Int,
    @SerializedName("paging") val paging: Map<String, Int>,
    @SerializedName("response") val response: TeamStatisticsDataSchema?
)
