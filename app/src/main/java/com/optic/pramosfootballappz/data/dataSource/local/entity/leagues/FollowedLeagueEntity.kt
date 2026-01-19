package com.optic.pramosfootballappz.data.dataSource.local.entity.leagues

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed_leagues")
data class FollowedLeagueEntity(
    @PrimaryKey val league_id: Int
)