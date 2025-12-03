package com.optic.ecommerceappmvvm.data.dataSource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.FixtureDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.LeagueDao
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.PlayerDao
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FixtureEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.LeagueEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.PlayerEntity

@Database(
    entities = [
        FixtureEntity::class,
        LeagueEntity::class,
        PlayerEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun fixtureDao(): FixtureDao
    abstract fun leagueDao(): LeagueDao
    abstract fun playerDao(): PlayerDao
}
