package com.optic.pramosfootballappz.domain.model.prode

import com.optic.pramosfootballappz.domain.model.League.League
import com.optic.pramosfootballappz.domain.model.fixture.FixtureResponse


data class UserPredictionSummaryEnriched(
    val userId: Int,
    val season: Int,
    val totalPoints: Int,
    val totalFixtures: Int,
    val rankingPosition: Int?,
    val leagues: List<LeaguePredictionSummaryEnriched>,
    val lastPrediction: LastPredictionSummaryEnriched?
)

data class LeaguePredictionSummaryEnriched(
    val league: League,
    val cantFixtures: Int,
    val totalPoints: Int,
    val fixture: FixturePredictionEnriched?
)

data class FixturePredictionEnriched(
    val prediction: FixturePredictionMini,
    val fixture: FixtureResponse
)

data class LastPredictionSummaryEnriched(
    val fixture: FixtureResponse,
    val createdAt: String
)
