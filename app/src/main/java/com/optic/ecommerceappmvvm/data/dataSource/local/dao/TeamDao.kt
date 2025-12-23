package com.optic.ecommerceappmvvm.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FollowedTeamEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(players: List<TeamEntity>)

    @Query("SELECT * FROM teams LIMIT 50")
    suspend fun getTeams(): List<TeamEntity>

    //@Query("DELETE FROM players")
    //suspend fun clearPlayers()

    // funciones para teams seguidos

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowedTeam(team: FollowedTeamEntity)

    @Query("""
    SELECT l.*
    FROM teams l
    INNER JOIN followed_teams f
        ON l.id = f.team_id
""")
    fun getFollowedTeams(): Flow<List<TeamEntity>>


    @Query("DELETE FROM followed_teams WHERE team_id = :teamId")
    suspend fun deleteFollowedTeam(teamId: Int)

    @Query("SELECT * FROM followed_teams")
    suspend fun getAllFollowedTeams(): List<FollowedTeamEntity>


}
