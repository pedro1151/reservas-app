package com.optic.ecommerceappmvvm.data.dataSource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.FixtureDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.FixturePredictionDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.LeagueDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.PlayerDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.TeamDao
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FixtureEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FixturePredictionEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FollowedLeagueEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FollowedPlayerEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FollowedTeamEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.LeagueEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.PlayerEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.TeamEntity

@Database(
    entities = [
        FixtureEntity::class,
        LeagueEntity::class,
        PlayerEntity::class,
        TeamEntity::class,
    FixturePredictionEntity::class,
    FollowedLeagueEntity::class,
    FollowedPlayerEntity::class,
        FollowedTeamEntity::class,

    ],
    version = 28,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun fixtureDao(): FixtureDao
    abstract fun leagueDao(): LeagueDao
    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
    abstract fun fixturePredictionDao(): FixturePredictionDao
}
