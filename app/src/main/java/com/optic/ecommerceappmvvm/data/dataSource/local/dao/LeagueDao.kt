package com.optic.ecommerceappmvvm.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.LeagueEntity

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
}
