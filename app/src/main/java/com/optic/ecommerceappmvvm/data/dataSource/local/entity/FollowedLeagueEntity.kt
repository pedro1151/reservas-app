package com.optic.ecommerceappmvvm.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed_leagues")
data class FollowedLeagueEntity(
    @PrimaryKey val league_id: Int
)