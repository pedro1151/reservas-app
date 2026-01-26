package com.optic.pramosreservasappz.data.dataSource.local.entity.prode

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fixture_predictions",
    indices = [
        Index(value = ["fixtureId"]),
        Index(value = ["leagueId"]),
        Index(value = ["round"]),
        Index(value = ["userId"])
    ]
)
data class FixturePredictionEntity(
    @PrimaryKey val fixtureId: Int,
    val leagueId: Int,
    val round: String,
    val userId: Int,
    val prediction: String,
    val goalsHome: Int? = null,
    val goalsAway: Int? = null,
    val leagueSeason: Int
)



