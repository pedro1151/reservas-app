package com.optic.ecommerceappmvvm.domain.model.prode

import com.google.gson.annotations.SerializedName

data class FixturePredictionRequest(

    @SerializedName("fixture_id") val fixtureId: Int,
    @SerializedName("league_id") val leagueId: Int,
    @SerializedName("league_season") val leagueSeason: Int,
    @SerializedName("round") val round: String,
    @SerializedName("user_id") val userId: Int,
    // "home", "draw", "away"
    @SerializedName("prediction") val prediction: String,
    @SerializedName("goals_home") val goalsHome: Int? = null,
    @SerializedName("goals_away") val goalsAway: Int? = null
)
