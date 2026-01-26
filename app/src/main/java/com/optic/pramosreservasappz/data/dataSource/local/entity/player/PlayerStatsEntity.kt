package com.optic.pramosreservasappz.data.dataSource.local.entity.player

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "player_statistics",
    indices = [
        Index(value = ["playerId"]),
        Index(value = ["season"]),
        Index(value = ["teamId"]),
        Index(value = ["leagueId"]),
        Index(value = ["playerId", "season"]),
        Index(value = ["playerId", "leagueId", "season"])
    ]
)
data class PlayerStatsEntity(

    // -------- Player base --------
    @PrimaryKey val id: Int,              // ID de la estadística (NO el playerId)
    val playerId: Int,
    // -------- Season --------
    val season: Int,

    // -------- Team --------
    val teamId: Int,
    val teamApiId: Int,
    val teamName: String,
    val teamLogo: String,

    // -------- League --------
    val leagueId: Int,
    val leagueApiId: Int,
    val leagueName: String,
    val leagueLogo: String,

    // -------- Games --------
    val gamesPosition: String?,
    val gamesAppearences: Int?,
    val gamesLineups: Int?,
    val gamesMinutes: Int?,
    val gamesNumber: Int?,
    val gamesRating: Double?,
    val gamesCaptain: Boolean?,

    // -------- Goals --------
    val goalsTotal: Int?,
    val goalsAssists: Int?,
    val goalsConceded: Int?,
    val goalsSaves: Int?,

    // -------- Passes --------
    val passesTotal: Int?,
    val passesKey: Int?,
    val passesAccuracy: Int?,

    // -------- Tackles --------
    val tacklesTotal: Int?,
    val tacklesBlocks: Int?,
    val tacklesInterceptions: Int?,

    // -------- Duels --------
    val duelsTotal: Int?,
    val duelsWon: Int?,

    // -------- Dribbles --------
    val dribblesAttempts: Int?,
    val dribblesSuccess: Int?,
    val dribblesPast: Int?,

    // -------- Fouls --------
    val foulsDrawn: Int?,
    val foulsCommitted: Int?,

    // -------- Shots --------
    val shotsTotal: Int?,
    val shotsOn: Int?,

    // -------- Substitutions --------
    val substitutesIn: Int?,
    val substitutesOut: Int?,
    val substitutesBench: Int?,

    // -------- Cards --------
    val cardsYellow: Int?,
    val cardsYellowRed: Int?,
    val cardsRed: Int?,

    // -------- Penalties --------
    val penaltyWon: Boolean?,
    val penaltyCommited: Boolean?,
    val penaltyScored: Int?,
    val penaltyMissed: Int?,
    val penaltySaved: Int?
)