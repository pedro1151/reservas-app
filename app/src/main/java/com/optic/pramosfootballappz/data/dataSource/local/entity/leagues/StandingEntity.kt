package com.optic.pramosfootballappz.data.dataSource.local.entity.leagues

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "standings",
    indices = [
        Index(value = ["leagueId", "season"]),
        Index(value = ["teamId"]),
        Index(value = ["leagueId", "season", "teamId"], unique = true)
    ]
)
data class StandingEntity(

    @PrimaryKey
    val id: Int,

    val leagueId: Int,
    val season: Int,

    // Team
    val teamId: Int,
    val teamName: String,
    val teamLogo: String,

    // Tabla
    val rank: Int,
    val points: Int,
    val goalsDiff: Int?,

    val groupName: String?,
    val form: String?,
    val status: String?,
    val description: String?,

    // ----- ALL
    val allPlayed: Int?,
    val allWin: Int?,
    val allDraw: Int?,
    val allLose: Int?,
    val allGoalsFor: Int?,
    val allGoalsAgainst: Int?,

    // ----- HOME
    val homePlayed: Int?,
    val homeWin: Int?,
    val homeDraw: Int?,
    val homeLose: Int?,
    val homeGoalsFor: Int?,
    val homeGoalsAgainst: Int?,

    // ----- AWAY
    val awayPlayed: Int?,
    val awayWin: Int?,
    val awayDraw: Int?,
    val awayLose: Int?,
    val awayGoalsFor: Int?,
    val awayGoalsAgainst: Int?,

    val updatedAt: String?
)
