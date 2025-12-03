package com.optic.ecommerceappmvvm.data.dataSource.local.mapper


import com.google.gson.Gson
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.LeagueEntity
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.administracion.Country

private val gson = Gson()

fun LeagueEntity.toDomain(): League =
    League(
        id = id,
        apiId = apiId,
        name = name,
        type = type,
        logo = logo,
        country = countryJson?.let { gson.fromJson(it, Country::class.java) }
    )

fun League.toEntity(): LeagueEntity =
    LeagueEntity(
        id = id,
        apiId = apiId,
        name = name,
        type = type,
        logo = logo,
        countryJson = country?.let { gson.toJson(it) }
    )
