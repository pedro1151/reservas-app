package com.optic.ecommerceappmvvm.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FollowedLeagueEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.LeagueEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface LeagueDao {

    @Query("""
        SELECT * FROM leagues
        WHERE (:name = '' OR name LIKE '%' || :name || '%')
        AND (:type = '' OR type = :type)
        AND (:country = '' OR countryJson LIKE '%' || :country || '%')
    """)
    suspend fun getLeagues(name: String, type: String, country: String): List<LeagueEntity>


    //Top Ligas
    @Query("SELECT * FROM leagues WHERE isTop = 1")
    suspend fun getTopLeagues(): List<LeagueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeagues(leagues: List<LeagueEntity>)

    @Query("SELECT COUNT(*) FROM leagues")
    suspend fun getTotalCount(): Int

    // entity para followed leagues
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowedLeague(league: FollowedLeagueEntity)

    /**
     * 🔥 QUERY CLAVE
     * Recupera ligas seguidas desde cache
     */
    @Query("""
    SELECT l.*
    FROM leagues l
    INNER JOIN followed_leagues f
        ON l.id = f.league_id
""")
    fun getFollowedLeaguesFromCache(): Flow<List<LeagueEntity>>



    @Query("DELETE FROM followed_leagues WHERE league_id = :leagueId")
    suspend fun deleteFollowedLeagueFromCache(leagueId: Int)
}
