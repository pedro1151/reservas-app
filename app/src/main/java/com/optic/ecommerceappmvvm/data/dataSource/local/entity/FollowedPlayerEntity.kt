package com.optic.ecommerceappmvvm.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed_players")
data class FollowedPlayerEntity(
    @PrimaryKey val player_id: Int
)