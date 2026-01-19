package com.optic.pramosfootballappz.data.dataSource.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.optic.pramosfootballappz.data.dataSource.local.entity.FixtureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FixtureDao {

    @Query("SELECT * FROM fixtures WHERE timestamp >= :startTs AND timestamp < :endTs ORDER BY timestamp ASC")
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


    // FIxtures asociados a predicciones de un usuario
    @Query(
        """
    SELECT f.*
    FROM fixtures f
    INNER JOIN fixture_predictions fp ON f.id = fp.fixtureId
    WHERE fp.userId = :userId
      AND fp.leagueSeason = :season
    ORDER BY f.timestamp DESC
    """
    )
    suspend fun getUserFixturePredictions(
        userId: Int,
        season: Int
    ): List<FixtureEntity>

    @Query("SELECT * FROM fixtures WHERE id = :fixtureId LIMIT 1")
    suspend fun getById(fixtureId: Int): FixtureEntity?


    // followed league fixtures or teams fixtures
    @Query(
        """
    SELECT f.*
    FROM fixtures f
    WHERE f.timestamp BETWEEN :start AND :end
      AND (
          EXISTS (
              SELECT 1
              FROM followed_leagues fl
              WHERE fl.league_id = f.leagueId
          )
          OR
          EXISTS (
              SELECT 1
              FROM followed_teams ft
              WHERE ft.team_id = f.homeTeamId
                 OR ft.team_id = f.awayTeamId
          )
      )
    ORDER BY f.timestamp ASC
    """
    )
    fun getFollowedFixturesTeamsOrLeagues(
        start: Long,
        end: Long
    ): Flow<List<FixtureEntity>>



}
