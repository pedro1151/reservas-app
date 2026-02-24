package com.optic.pramosreservasappz.domain.model.fixture

import com.google.gson.annotations.SerializedName
import com.optic.pramosreservasappz.domain.model.administracion.Country

data class League(
    @SerializedName("id") val id: Int,
    @SerializedName("api_id") val apiId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: Country?,
    @SerializedName("logo") val logo: String
)