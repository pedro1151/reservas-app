package com.optic.ecommerceappmvvm.data.dataSource.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FixtureEntity

@Dao
interface FixtureDao {

    @Query("SELECT * FROM fixtures WHERE timestamp >= :startTs AND timestamp < :endTs ORDER BY timestamp DESC")
    suspend fun getFixturesByDateRange(startTs: Long, endTs: Long): List<FixtureEntity>

    @Query("SELECT * FROM fixtures WHERE timestamp >= :start AND timestamp < :end AND leagueId = :leagueId AND leagueSeason = :season ORDER BY timestamp DESC")
    suspend fun getFixturesByLeague(start: Long, end: Long, leagueId: Int, season: Int): List<FixtureEntity>

    @Query("SELECT * FROM fixtures WHERE timestamp >= :start AND timestamp < :end AND ( awayTeamId = :teamId  OR homeTeamId = :teamId )")
    suspend fun getFixturesByTeam(start: Long, end: Long, teamId: Int): List<FixtureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixtures(fixtures: List<FixtureEntity>)

    // proximo partido de un equipo

    @Query("""
    SELECT *
    FROM fixtures
    WHERE
        (:teamId = homeTeamId OR :teamId = awayTeamId)
        AND timestamp > :now
        AND statusShort IN ('NS', 'TBD', 'PST', 'SUSP', 'ABD', 'WO')
    ORDER BY timestamp ASC
    LIMIT 1
""")
    suspend fun getNextTeamFixture(
        teamId: Int,
        now: Long
    ): FixtureEntity


    // ultimos partidos de un equipo
    @Query(
        """
    SELECT *
    FROM fixtures
    WHERE 
        (homeTeamId = :teamId OR awayTeamId = :teamId)
        AND statusShort IN ('FT', 'AET', 'PEN', 'AWD', 'WO')
    ORDER BY timestamp DESC
    LIMIT :limit
    """
    )
    suspend fun getLastTeamFixtures(
        teamId: Int,
        limit: Int
    ): List<FixtureEntity>

}
