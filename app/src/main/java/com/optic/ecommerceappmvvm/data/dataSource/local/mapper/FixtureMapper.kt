package com.optic.ecommerceappmvvm.data.dataSource.local.mapper

import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FixtureEntity
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.fixture.League
import com.optic.ecommerceappmvvm.domain.model.fixture.Team
import com.optic.ecommerceappmvvm.domain.model.fixture.Venue

fun FixtureResponse.toEntity(): FixtureEntity {
    return FixtureEntity(
        id = id,
        apiId = apiId,
        referee = referee,
        timezone = timezone,
        date = date,
        timestamp = timestamp,
        periodsFirst = periodsFirst,
        periodsSecond = periodsSecond,

        venueId = venue.id,
        venueName = venue.name,
        venueCity = venue.city,
        venueImage = venue.image,

        statusLong = statusLong,
        statusShort = statusShort,
        statusElapsed = statusElapsed,
        statusExtra = statusExtra,

        leagueId = league.id,
        leagueName = league.name,
        leagueCountryId = league.countryId,
        leagueLogo = league.logo,
        leagueSeason = leagueSeason,

        homeTeamId = teamHome.id,
        homeTeamName = teamHome.name,
        homeTeamLogo = teamHome.logo,

        awayTeamId = teamAway.id,
        awayTeamName = teamAway.name,
        awayTeamLogo = teamAway.logo,

        teamWinnerId = teamWinnerId,
        teamWinnerName = teamWinner?.name,
        teamWinnerLogo = teamWinner?.logo,

        goalsHome = goalsHome,
        goalsAway = goalsAway,
        scoreHalftimeHome = scoreHalftimeHome,
        scoreHalftimeAway = scoreHalftimeAway,
        scoreFulltimeHome = scoreFulltimeHome,
        scoreFulltimeAway = scoreFulltimeAway,
        scoreExtratimeHome = scoreExtratimeHome,
        scoreExtratimeAway = scoreExtratimeAway,
        scorePenaltyHome = scorePenaltyHome,
        scorePenaltyAway = scorePenaltyAway
    )
}

fun FixtureEntity.toDomain(): FixtureResponse {
    return FixtureResponse(
        id = id,
        apiId = apiId,
        referee = referee,
        timezone = timezone,
        date = date,
        timestamp = timestamp,
        periodsFirst = periodsFirst,
        periodsSecond = periodsSecond,

        venue = Venue(
            id = venueId,
            apiId = 0, // si no se guarda, default
            name = venueName?: "",
            city = venueCity?: "",
            image = venueImage?: ""
        ),
        venueId = venueId,

        statusLong = statusLong,
        statusShort = statusShort,
        statusElapsed = statusElapsed,
        statusExtra = statusExtra,

        league = League(
            id = leagueId,
            apiId = 0, // default
            name = leagueName,
            countryId = leagueCountryId,
            logo = leagueLogo
        ),
        leagueSeason = leagueSeason,

        teamHome = Team(
            id = homeTeamId,
            name = homeTeamName,
            logo = homeTeamLogo
        ),
        teamAway = Team(
            id = awayTeamId,
            name = awayTeamName,
            logo = awayTeamLogo
        ),
        teamWinner = if (teamWinnerId != null) Team(
            id = teamWinnerId,
            name = teamWinnerName ?: "",
            logo = teamWinnerLogo ?: ""
        ) else null,
        teamWinnerId = teamWinnerId,

        goalsHome = goalsHome,
        goalsAway = goalsAway,
        scoreHalftimeHome = scoreHalftimeHome,
        scoreHalftimeAway = scoreHalftimeAway,
        scoreFulltimeHome = scoreFulltimeHome,
        scoreFulltimeAway = scoreFulltimeAway,
        scoreExtratimeHome = scoreExtratimeHome,
        scoreExtratimeAway = scoreExtratimeAway,
        scorePenaltyHome = scorePenaltyHome,
        scorePenaltyAway = scorePenaltyAway
    )
}
