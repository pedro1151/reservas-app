package com.optic.ecommerceappmvvm.data.dataSource.local.mapper



import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FixturePredictionEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.TeamEntity
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionRequest
import com.optic.ecommerceappmvvm.domain.model.prode.FixturePredictionResponse


fun FixturePredictionResponse.toEntity(): FixturePredictionEntity =
    FixturePredictionEntity(
        fixtureId = this.fixtureId,
        leagueId = this.leagueId,
        round = this.round,
        prediction = this.prediction,
        goalsHome = this.goalsHome,
        goalsAway = this.goalsAway,
        userId = this.userId,
        leagueSeason = this.leagueSeason

    )

fun FixturePredictionEntity.toDomain(): FixturePredictionResponse =
    FixturePredictionResponse(
        id = 0,
        fixtureId = this.fixtureId,
        leagueId = this.leagueId,
        round = this.round,
        prediction = this.prediction,
        goalsHome = this.goalsHome,
        goalsAway = this.goalsAway,
        userId =  this.userId,
        leagueSeason = this.leagueSeason
    )

fun FixturePredictionRequest.toEntity(): FixturePredictionEntity {
    return FixturePredictionEntity(
        fixtureId = this.fixtureId,
        leagueId = this.leagueId,
        round = this.round,
        userId = -1,
        prediction = this.prediction,
        goalsHome = this.goalsHome,
        goalsAway = this.goalsAway,
        leagueSeason = this.leagueSeason
    )
}

fun FixturePredictionEntity.toRequest(): FixturePredictionRequest {
    return FixturePredictionRequest(
        leagueSeason = this.leagueSeason,
        fixtureId = this.fixtureId,
        leagueId = this.leagueId,
        round = this.round,
        userId = -1,
        prediction = this.prediction,
        goalsHome = this.goalsHome,
        goalsAway = this.goalsAway
    )
}