package com.optic.ecommerceappmvvm.data.dataSource.local.mapper


import com.google.gson.Gson
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.leagues.FollowedLeagueEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.leagues.LeagueEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.leagues.StandingEntity
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.administracion.Country
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueRequest
import com.optic.ecommerceappmvvm.domain.model.standing.StandingResponse

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


//standings

fun StandingResponse.toEntity(): StandingEntity =
    StandingEntity(
        id = id,
        leagueId = leagueId,
        season = season,

        teamId = team.id,
        teamName = team.name,
        teamLogo = team.logo,

        rank = rank,
        points = points,
        goalsDiff = goalsDiff,

        groupName = group,
        form = form,
        status = status,
        description = description,

        allPlayed = allPlayed,
        allWin = allWin,
        allDraw = allDraw,
        allLose = allLose,
        allGoalsFor = allGoalsFor,
        allGoalsAgainst = allGoalsAgainst,

        homePlayed = homePlayed,
        homeWin = homeWin,
        homeDraw = homeDraw,
        homeLose = homeLose,
        homeGoalsFor = homeGoalsFor,
        homeGoalsAgainst = homeGoalsAgainst,

        awayPlayed = awayPlayed,
        awayWin = awayWin,
        awayDraw = awayDraw,
        awayLose = awayLose,
        awayGoalsFor = awayGoalsFor,
        awayGoalsAgainst = awayGoalsAgainst,

        updatedAt = update
    )

fun List<StandingResponse>.toEntities(): List<StandingEntity> =
    map { it.toEntity() }
fun StandingEntity.toDomain(): StandingResponse =
    StandingResponse(
        id = id,
        leagueId = leagueId,
        season = season,

        team = StandingResponse.Team(
            id = teamId,
            name = teamName,
            logo = teamLogo
        ),

        rank = rank,
        points = points,
        goalsDiff = goalsDiff,

        group = groupName,
        form = form,
        status = status,
        description = description,

        allPlayed = allPlayed,
        allWin = allWin,
        allDraw = allDraw,
        allLose = allLose,
        allGoalsFor = allGoalsFor,
        allGoalsAgainst = allGoalsAgainst,

        homePlayed = homePlayed,
        homeWin = homeWin,
        homeDraw = homeDraw,
        homeLose = homeLose,
        homeGoalsFor = homeGoalsFor,
        homeGoalsAgainst = homeGoalsAgainst,

        awayPlayed = awayPlayed,
        awayWin = awayWin,
        awayDraw = awayDraw,
        awayLose = awayLose,
        awayGoalsFor = awayGoalsFor,
        awayGoalsAgainst = awayGoalsAgainst,

        update = updatedAt
    )
fun List<StandingEntity>.toDomain(): List<StandingResponse> =
    map { it.toDomain() }

