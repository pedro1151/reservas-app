package com.optic.ecommerceappmvvm.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.leagues.StandingEntity
import com.optic.ecommerceappmvvm.domain.model.standing.StandingResponse

@Entity(tableName = "leagues")
data class LeagueEntity(
    @PrimaryKey val id: Int,
    val apiId: Int,
    val name: String,
    val type: String,
    val logo: String?,
    val isTop: Boolean,
    val countryJson: String? // Country serializado
)

