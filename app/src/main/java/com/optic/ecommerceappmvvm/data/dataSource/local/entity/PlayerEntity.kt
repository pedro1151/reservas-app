package com.optic.ecommerceappmvvm.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val apiId: Int?,
    val name: String?,
    val firstname: String?,
    val lastname: String?,
    val age: String?,
    val nationality: String?,
    val birthDate: String?,
    val birthPlace: String?,
    val birthCountry: String?,
    val height: String?,
    val weight: String?,
    val photo: String?,

    // last team
    val lastTeamId: Int?,
    val lastTeamName: String?,
    val lastTeamCode: String?,
    val lastTeamLogo: String?
)
