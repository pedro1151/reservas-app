package com.optic.pramosreservasappz.domain.model.player.stats

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Team(
    @SerializedName("id") val id: Int,
    @SerializedName("api_id") val apiId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String
) : Serializable