package com.optic.pramosreservasappz.domain.model.fixture

import com.google.gson.annotations.SerializedName

data class League(
    @SerializedName("id") val id: Int,
    @SerializedName("api_id") val apiId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("country_id") val countryId: Int,
    @SerializedName("logo") val logo: String
)