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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixtures(fixtures: List<FixtureEntity>)
}
