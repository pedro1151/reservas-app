package com.optic.ecommerceappmvvm.data.dataSource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.optic.ecommerceappmvvm.data.dataSource.local.dao.FixtureDao
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FixtureEntity

@Database(
    entities = [FixtureEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun fixtureDao(): FixtureDao
}
