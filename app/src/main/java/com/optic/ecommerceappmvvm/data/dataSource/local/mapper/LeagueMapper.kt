package com.optic.ecommerceappmvvm.data.dataSource.local.mapper


import com.google.gson.Gson
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FixturePredictionEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FollowedLeagueEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.LeagueEntity
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.administracion.Country
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueRequest
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueResponse
import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionRequest

private val gson = Gson()

fun LeagueEntity.toDomain(): League =
    League(
        id = id,
        apiId = apiId,
        name = name,
        type = type,
        logo = logo,
        isTop = isTop,
        country = countryJson?.let { gson.fromJson(it, Country::class.java) }
    )

fun League.toEntity(): LeagueEntity =
    LeagueEntity(
        id = id,
        apiId = apiId,
        name = name,
        type = type,
        logo = logo,
        isTop = isTop,
        countryJson = country?.let { gson.toJson(it) }
    )

fun FollowedLeagueEntity.toRequest(): FollowedLeagueRequest {
    return FollowedLeagueRequest(
        league_id = this.league_id
    )
}

fun FollowedLeagueRequest.toEntity(): FollowedLeagueEntity {
    return FollowedLeagueEntity(
        league_id = this.league_id?:0
    )
}

