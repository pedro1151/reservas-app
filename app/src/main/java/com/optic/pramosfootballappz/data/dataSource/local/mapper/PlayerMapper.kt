package com.optic.pramosfootballappz.data.dataSource.local.mapper

import com.optic.pramosfootballappz.data.dataSource.local.entity.player.FollowedPlayerEntity
import com.optic.pramosfootballappz.data.dataSource.local.entity.player.PlayerEntity
import com.optic.pramosfootballappz.data.dataSource.local.entity.player.PlayerStatsEntity
import com.optic.pramosfootballappz.domain.model.followed.FollowedPlayerRequest
import com.optic.pramosfootballappz.domain.model.player.Player
import com.optic.pramosfootballappz.domain.model.player.TeamMiniResponse
import com.optic.pramosfootballappz.domain.model.player.stats.League
import com.optic.pramosfootballappz.domain.model.player.stats.PlayerStats
import com.optic.pramosfootballappz.domain.model.player.stats.PlayerWithStats
import com.optic.pramosfootballappz.domain.model.player.stats.Team



fun Player.toEntity(): PlayerEntity =
    PlayerEntity(
        id = this.id ?: 0,
        //apiId = this.apiId,
        name = this.name,
        firstname = this.firstname,
        lastname = this.lastname,
        photo = this.photo,

        lastTeamId = this.lastTeam?.id,
        lastTeamName = this.lastTeam?.name,
        lastTeamCode = this.lastTeam?.code,
        lastTeamLogo = this.lastTeam?.logo
    )

fun PlayerEntity.toDomain(): Player =
    Player(
        id = this.id,
        //apiId = this.apiId,
        name = this.name,
        firstname = this.firstname,
        lastname = this.lastname,
        photo = this.photo,
        lastTeam = this.lastTeamId?.let {
            TeamMiniResponse(
                id = it,
                name = this.lastTeamName,
                code = this.lastTeamCode,
                logo = this.lastTeamLogo
            )
        }
    )

fun FollowedPlayerEntity.toRequest(): FollowedPlayerRequest {
    return FollowedPlayerRequest (
        player_id = this.player_id
    )
}

fun FollowedPlayerRequest.toEntity(): FollowedPlayerEntity {
    return FollowedPlayerEntity(
        player_id = this.player_id?:0
    )
}


// stats

fun PlayerWithStats.toEntities(): List<PlayerStatsEntity> {
    return statistics.map { stat ->
        PlayerStatsEntity(
            id = stat.id,
            playerId = stat.playerId,
            season = stat.season,

            teamId = stat.team.id,
            teamApiId = stat.team.apiId,
            teamName = stat.team.name,
            teamLogo = stat.team.logo,

            leagueId = stat.league.id,
            leagueApiId = stat.league.apiId,
            leagueName = stat.league.name,
            leagueLogo = stat.league.logo,

            gamesPosition = stat.gamesPosition,
            gamesAppearences = stat.gamesAppearences,
            gamesLineups = stat.gamesLineups,
            gamesMinutes = stat.gamesMinutes,
            gamesNumber = stat.gamesNumber,
            gamesRating = stat.gamesRating,
            gamesCaptain = stat.gamesCaptain,

            goalsTotal = stat.goalsTotal,
            goalsAssists = stat.goalsTotalAssists,
            goalsConceded = stat.goalsConceded,
            goalsSaves = stat.goalsSaves,

            passesTotal = stat.passesTotal,
            passesKey = stat.passesKey,
            passesAccuracy = stat.passesAccuracy,

            tacklesTotal = stat.tacklesTotal,
            tacklesBlocks = stat.tacklesBlocks,
            tacklesInterceptions = stat.tacklesInterceptions,

            duelsTotal = stat.duelsTotal,
            duelsWon = stat.duelsWon,

            dribblesAttempts = stat.dribblesAttempts,
            dribblesSuccess = stat.dribblesSuccess,
            dribblesPast = stat.dribblesPast,

            foulsDrawn = stat.foulsDrawn,
            foulsCommitted = stat.foulsCommitted,

            shotsTotal = stat.shotsTotal,
            shotsOn = stat.shotsOn,

            substitutesIn = stat.substitutesIn,
            substitutesOut = stat.substitutesOut,
            substitutesBench = stat.substitutesBench,

            cardsYellow = stat.cardsYellow,
            cardsYellowRed = stat.cardsYellowRed,
            cardsRed = stat.cardsRed,

            penaltyWon = stat.penaltyWon,
            penaltyCommited = stat.penaltyCommited,
            penaltyScored = stat.penaltyScored,
            penaltyMissed = stat.penaltyMissed,
            penaltySaved = stat.penaltySaved
        )
    }
}

fun List<PlayerStatsEntity>.toDomain(): PlayerWithStats {
    val first = first()

    return PlayerWithStats(
        statistics = map { it.toPlayerStatsDomain() }
    )
}

fun PlayerStatsEntity.toPlayerStatsDomain(): PlayerStats {
    return PlayerStats(
        id = id,
        playerId = playerId,
        season = season,

        team = Team(
            id = teamId,
            apiId = teamApiId,
            name = teamName,
            logo = teamLogo
        ),

        league = League(
            id = leagueId,
            apiId = leagueApiId,
            name = leagueName,
            logo = leagueLogo
        ),

        gamesPosition = gamesPosition,
        gamesAppearences = gamesAppearences,
        gamesLineups = gamesLineups,
        gamesMinutes = gamesMinutes,
        gamesNumber = gamesNumber,
        gamesRating = gamesRating,
        gamesCaptain = gamesCaptain,

        goalsTotal = goalsTotal,
        goalsTotalAssists = goalsAssists,
        goalsConceded = goalsConceded,
        goalsSaves = goalsSaves,

        passesTotal = passesTotal,
        passesKey = passesKey,
        passesAccuracy = passesAccuracy,

        tacklesTotal = tacklesTotal,
        tacklesBlocks = tacklesBlocks,
        tacklesInterceptions = tacklesInterceptions,

        duelsTotal = duelsTotal,
        duelsWon = duelsWon,

        dribblesAttempts = dribblesAttempts,
        dribblesSuccess = dribblesSuccess,
        dribblesPast = dribblesPast,

        foulsDrawn = foulsDrawn,
        foulsCommitted = foulsCommitted,

        shotsTotal = shotsTotal,
        shotsOn = shotsOn,

        substitutesIn = substitutesIn,
        substitutesOut = substitutesOut,
        substitutesBench = substitutesBench,

        cardsYellow = cardsYellow,
        cardsYellowRed = cardsYellowRed,
        cardsRed = cardsRed,

        penaltyWon = penaltyWon,
        penaltyCommited = penaltyCommited,
        penaltyScored = penaltyScored,
        penaltyMissed = penaltyMissed,
        penaltySaved = penaltySaved
    )
}
