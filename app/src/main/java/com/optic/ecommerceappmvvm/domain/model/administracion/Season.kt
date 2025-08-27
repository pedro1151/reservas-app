package com.optic.ecommerceappmvvm.domain.model.administracion

import com.google.gson.annotations.SerializedName


data class Season(
    @SerializedName("id") val id: Int,
    @SerializedName("year") val year: Int,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("current") val current: Boolean,
    @SerializedName("coverage_fixtures_events") val coverageFixturesEvents: Boolean,
    @SerializedName("coverage_fixtures_lineups") val coverageFixturesLineups: Boolean,
    @SerializedName("coverage_fixtures_statistics_fixtures") val coverageFixturesStatisticsFixtures: Boolean,
    @SerializedName("coverage_fixtures_statistics_players") val coverageFixturesStatisticsPlayers: Boolean,
    @SerializedName("coverage_standings") val coverageStandings: Boolean,
    @SerializedName("coverage_players") val coveragePlayers: Boolean,
    @SerializedName("coverage_top_scorers") val coverageTopScorers: Boolean,
    @SerializedName("coverage_top_assists") val coverageTopAssists: Boolean,
    @SerializedName("coverage_top_cards") val coverageTopCards: Boolean,
    @SerializedName("coverage_injuries") val coverageInjuries: Boolean,
    @SerializedName("coverage_predictions") val coveragePredictions: Boolean,
    @SerializedName("coverage_odds") val coverageOdds: Boolean,
    @SerializedName("league_id") val leagueId: Int
)