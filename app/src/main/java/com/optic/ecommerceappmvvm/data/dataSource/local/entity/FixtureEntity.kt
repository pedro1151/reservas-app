package com.optic.ecommerceappmvvm.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fixtures",
    indices = [
        Index(value = ["timestamp"]),
        Index(value = ["id"]),
        Index(value = ["leagueId", "leagueSeason"]),
        Index(value = ["timestamp", "leagueId", "leagueSeason"])
    ]
)
data class FixtureEntity(
    @PrimaryKey val id: Int,
    val apiId: Int,
    val referee: String?,
    val timezone: String,
    val date: String,
    val timestamp: Long,
    val periodsFirst: Long?,
    val periodsSecond: Long?,

    // Venue simplificado
    val venueId: Int,
    val venueName: String?,
    val venueCity: String?,
    val venueImage: String?,

    // Status
    val statusLong: String,
    val statusShort: String,
    val statusElapsed: Int?,
    val statusExtra: String?,

    // League simplificado
    val leagueId: Int,
    val leagueName: String,
    val leagueCountryId: Int,
    val leagueLogo: String,
    val leagueSeason: Int,

    // Team home simplificado
    val homeTeamId: Int,
    val homeTeamName: String,
    val homeTeamLogo: String,

    // Team away simplificado
    val awayTeamId: Int,
    val awayTeamName: String,
    val awayTeamLogo: String,

    // Team winner (opcional)
    val teamWinnerId: Int?,
    val teamWinnerName: String?,
    val teamWinnerLogo: String?,

    // Goals / Scores
    val goalsHome: Int,
    val goalsAway: Int,
    val scoreHalftimeHome: Int,
    val scoreHalftimeAway: Int,
    val scoreFulltimeHome: Int,
    val scoreFulltimeAway: Int,
    val scoreExtratimeHome: Int?,
    val scoreExtratimeAway: Int?,
    val scorePenaltyHome: Int?,
    val scorePenaltyAway: Int?
)
