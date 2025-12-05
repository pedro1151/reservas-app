package com.optic.ecommerceappmvvm.data.dataSource.local.mapper


import com.optic.ecommerceappmvvm.data.dataSource.local.entity.TeamEntity
import com.optic.ecommerceappmvvm.domain.model.Team


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