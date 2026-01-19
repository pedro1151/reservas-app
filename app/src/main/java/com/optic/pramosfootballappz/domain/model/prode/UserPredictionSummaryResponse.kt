package com.optic.pramosfootballappz.domain.model.prode

import com.google.gson.annotations.SerializedName



data class UserPredictionSummaryResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("season") val season: Int,
    @SerializedName("total_points") val totalPoints: Int,
    @SerializedName("total_fixtures") val totalFixtures: Int,
    @SerializedName("ranking_position") val rankingPosition: Int?,
    @SerializedName("leagues") val leagues: List<LeaguePredictionSummary>,
    @SerializedName("last_prediction") val lastPrediction: LastPredictionSummary?
)

data class FixturePredictionMini(
    @SerializedName("fixture_id") val fixtureId: Int,
    @SerializedName("points_awarded") val pointsAwarded: Int?,
    @SerializedName("round") val round: String,
    @SerializedName("prediction") val prediction: String,
    @SerializedName("goals_home") val goalsHome: Int?,
    @SerializedName("goals_away") val goalsAway: Int?,
    @SerializedName("is_correct") val isCorrect: Boolean?,
    @SerializedName("is_locked") val isLocked: Boolean
)

data class LeaguePredictionSummary(
    @SerializedName("league_id") val leagueId: Int,
    @SerializedName("cant_fixtures") val cantFixtures: Int,
    @SerializedName("total_points") val totalPoints: Int,
    @SerializedName("fixture_prediction") val fixture: FixturePredictionMini?
)

data class LastPredictionSummary(
    @SerializedName("fixture_id") val fixtureId: Int,
    @SerializedName("created_at") val createdAt: String
)
