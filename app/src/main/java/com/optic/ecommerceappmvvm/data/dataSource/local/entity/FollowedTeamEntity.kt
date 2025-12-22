package com.optic.ecommerceappmvvm.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed_teams")
data class FollowedTeamEntity(
    @PrimaryKey val team_id: Int
)