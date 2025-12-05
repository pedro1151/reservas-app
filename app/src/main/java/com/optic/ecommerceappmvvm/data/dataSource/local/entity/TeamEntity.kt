package com.optic.ecommerceappmvvm.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "teams",
    indices = [
        Index(value = ["name"]),
        Index(value = ["id"]),
        Index(value = ["country"])
    ]
)
data class TeamEntity(
    @PrimaryKey val id: Int,
    //val apiId: Int? = null,
    val name: String,
    val country: String? = null,
    //val founded: Int? = null,
    //val code: String? = null,
    //val national: Boolean? = null,
    val logo: String? = null
)
