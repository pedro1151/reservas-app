package com.optic.ecommerceappmvvm.data.dataSource.local.mapper


import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FollowedPlayerEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FollowedTeamEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.TeamEntity
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedPlayerRequest
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedTeamRequest


fun Team.toEntity(): TeamEntity =
    TeamEntity(
        id = this.id ?: 0,
        //apiId = this.apiId,
        name = this.name,
        country = this.country,
        //founded = this.founded,
        //code = this.code,
        //national = this.national,
        logo = this.logo
    )

fun TeamEntity.toDomain(): Team =
    Team(
        id = this.id ?: 0,
        //apiId = this.apiId,
        name = this.name,
        country = this.country,
        //founded = this.founded,
        //code = this.code,
        //national = this.national,
        logo = this.logo
    )

fun FollowedTeamEntity.toRequest(): FollowedTeamRequest {
    return FollowedTeamRequest (
        team_id = this.team_id
    )
}

fun FollowedTeamRequest.toEntity(): FollowedTeamEntity {
    return FollowedTeamEntity(
        team_id = this.team_id ?: 0
    )
}