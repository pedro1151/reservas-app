package com.optic.pramosfootballappz.domain.model.administracion

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LeagueRound(
    @SerializedName("id") val id: Int,
    @SerializedName("round_name") val roundName: String,
    @SerializedName("is_current") var isCurrent: Boolean,
    @SerializedName("start_date") var startDate: String,
    @SerializedName("end_date") var endDate: String

) : Serializable