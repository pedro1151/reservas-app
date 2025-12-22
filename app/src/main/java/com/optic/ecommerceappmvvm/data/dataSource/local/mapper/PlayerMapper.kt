package com.optic.ecommerceappmvvm.data.dataSource.local.mapper

import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FollowedLeagueEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.FollowedPlayerEntity
import com.optic.ecommerceappmvvm.data.dataSource.local.entity.PlayerEntity
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedLeagueRequest
import com.optic.ecommerceappmvvm.domain.model.followed.FollowedPlayerRequest
import com.optic.ecommerceappmvvm.domain.model.player.Player
import com.optic.ecommerceappmvvm.domain.model.player.TeamMiniResponse

fun Player.toEntity(): PlayerEntity =
    PlayerEntity(
        id = this.id ?: 0,
        //apiId = this.apiId,
        name = this.name,
        firstname = this.firstname,
        lastname = this.lastname,
        //age = this.age,
        //nationality = this.nationality,
        //birthDate = this.birthDate,
        //birthPlace = this.birthPlace,
        //birthCountry = this.birthCountry,
        //height = this.height,
        //weight = this.weight,
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
        //age = this.age,
        //nationality = this.nationality,
        //birthDate = this.birthDate,
        //birthPlace = this.birthPlace,
        //birthCountry = this.birthCountry,
        //eight = this.height,
        //weight = this.weight,
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
