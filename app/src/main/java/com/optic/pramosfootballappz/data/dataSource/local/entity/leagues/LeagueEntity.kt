package com.optic.pramosfootballappz.data.dataSource.local.entity.leagues

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leagues")
data class LeagueEntity(
    @PrimaryKey val id: Int,
    val apiId: Int,
    val name: String,
    val type: String,
    val logo: String?,
    val isTop: Boolean,
    val countryJson: String? // Country serializado
)

