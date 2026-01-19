package com.optic.pramosfootballappz.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.optic.pramosfootballappz.data.dataSource.local.entity.player.FollowedPlayerEntity
import com.optic.pramosfootballappz.data.dataSource.local.entity.player.PlayerEntity
import com.optic.pramosfootballappz.data.dataSource.local.entity.player.PlayerStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Query("SELECT * FROM players LIMIT 50")
    suspend fun getPlayers(): List<PlayerEntity>


    // funciones para players seguidos

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowedPlayer(player: FollowedPlayerEntity)

    @Query("""
    SELECT l.*
    FROM players l
    INNER JOIN followed_players f
        ON l.id = f.player_id
""")
    fun getFollowedPlayers(): Flow<List<PlayerEntity>>


    @Query("DELETE FROM followed_players WHERE player_id = :playerId")
    suspend fun deleteFollowedPlayer(playerId: Int)

    @Query("SELECT * FROM followed_players")
    suspend fun getAllFollowedPlayers(): List<FollowedPlayerEntity>

    // stats
    @Query("SELECT * FROM player_statistics WHERE playerId = :playerId and season = :season")
    suspend fun getPlayerStats(playerId:Int, season:Int): List<PlayerStatsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerStats(playerStats: List<PlayerStatsEntity>)

}
