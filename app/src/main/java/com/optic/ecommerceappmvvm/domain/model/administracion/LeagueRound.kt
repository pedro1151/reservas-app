package com.optic.ecommerceappmvvm.domain.model.administracion

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LeagueRound(
    @SerializedName("id") val id: Int,
    @SerializedName("round_name") val roundName: String,
    @SerializedName("is_current") val isCurrent: Boolean
) : Serializable