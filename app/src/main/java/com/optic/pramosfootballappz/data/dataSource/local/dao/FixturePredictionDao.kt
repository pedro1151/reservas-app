package com.optic.pramosfootballappz.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.optic.pramosfootballappz.data.dataSource.local.entity.prode.FixturePredictionEntity

@Dao
interface FixturePredictionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixturePredictions(
        fixtures: List<FixturePredictionEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixturePrediction(
        fixture: FixturePredictionEntity
    )

    @Query("SELECT * FROM fixture_predictions")
    suspend fun getAll(): List<FixturePredictionEntity>


    @Query("DELETE FROM fixture_predictions")
    suspend fun clearAll()
}