package com.optic.ecommerceappmvvm.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.TeamEntity

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(players: List<TeamEntity>)

    @Query("SELECT * FROM teams LIMIT 50")
    suspend fun getTeams(): List<TeamEntity>

    //@Query("DELETE FROM players")
    //suspend fun clearPlayers()
}
