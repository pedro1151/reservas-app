package com.optic.ecommerceappmvvm.domain.model.fixture

import com.google.gson.annotations.SerializedName

data class FixtureResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("api_id") val apiId: Int,
    @SerializedName("referee") val referee: String?,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("date") val date: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("periods_first") val periodsFirst: Long?,
    @SerializedName("periods_second") val periodsSecond: Long?,
    @SerializedName("venue") val venue: Venue,
    @SerializedName("venue_id") val venueId: Int,
    @SerializedName("status_long") val statusLong: String,
    @SerializedName("status_short") val statusShort: String,
    @SerializedName("status_elapsed") val statusElapsed: Int?,
    @SerializedName("status_extra") val statusExtra: String?,
    @SerializedName("league") val league: League,
    @SerializedName("league_season") val leagueSeason: Int,
    @SerializedName("league_round") val leagueRound: String?,
    @SerializedName("team_home") val teamHome: Team,
    @SerializedName("team_away") val teamAway: Team,
    @SerializedName("team_winner") val teamWinner: Team?,
    @SerializedName("team_winner_id") val teamWinnerId: Int?,
    @SerializedName("goals_home") val goalsHome: Int,
    @SerializedName("goals_away") val goalsAway: Int,
    @SerializedName("score_halftime_home") val scoreHalftimeHome: Int,
    @SerializedName("score_halftime_away") val scoreHalftimeAway: Int,
    @SerializedName("score_fulltime_home") val scoreFulltimeHome: Int,
    @SerializedName("score_fulltime_away") val scoreFulltimeAway: Int,
    @SerializedName("score_extratime_home") val scoreExtratimeHome: Int?,
    @SerializedName("score_extratime_away") val scoreExtratimeAway: Int?,
    @SerializedName("score_penalty_home") val scorePenaltyHome: Int?,
    @SerializedName("score_penalty_away") val scorePenaltyAway: Int?
)

data class Venue(
    @SerializedName("id") val id: Int,
    @SerializedName("api_id") val apiId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("city") val city: String,
    @SerializedName("image") val image: String
)
