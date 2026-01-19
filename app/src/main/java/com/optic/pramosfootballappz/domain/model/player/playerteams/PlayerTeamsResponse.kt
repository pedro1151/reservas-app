package com.optic.pramosfootballappz.domain.model.player.playerteams

import com.google.gson.annotations.SerializedName
import com.optic.pramosfootballappz.domain.model.Team
import com.optic.pramosfootballappz.domain.model.player.Player
import java.io.Serializable

data class PlayerTeamsResponse(
    @SerializedName("player") val player: Player,
    @SerializedName("teams") val teams: List<TeamsWithSeasonsResponse>
) : Serializable

data class TeamsWithSeasonsResponse(
    @SerializedName("team") val team: Team,
    @SerializedName("seasons") val seasons: List<Int>
) : Serializable