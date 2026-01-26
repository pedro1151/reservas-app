package com.optic.pramosreservasappz.domain.model.player.playerteams


import com.google.gson.annotations.SerializedName
import com.optic.pramosreservasappz.domain.model.Team
import com.optic.pramosreservasappz.domain.model.player.Player
import java.io.Serializable

data class PlayerLastTeamResponse(
    @SerializedName("player") val player: Player,
    @SerializedName("last_team") val lastTeam: Team
) : Serializable

