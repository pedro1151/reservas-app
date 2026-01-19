package com.optic.pramosfootballappz.domain.model.player.stats


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlayerWithStats(
    @SerializedName("statistics") val statistics: List<PlayerStats>
) : Serializable


