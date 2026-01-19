package com.optic.pramosfootballappz.domain.model.League

import com.google.gson.annotations.SerializedName
import com.optic.pramosfootballappz.domain.model.administracion.Country
import com.optic.pramosfootballappz.domain.model.administracion.LeagueRound
import com.optic.pramosfootballappz.domain.model.administracion.Season

data class LeagueCompleteResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("api_id") val apiId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("logo") val logo: String,
    @SerializedName("country") val country: Country,
    @SerializedName("seasons") val seasons: List<Season>,
    @SerializedName("rounds") val rounds: List<LeagueRound>

)
