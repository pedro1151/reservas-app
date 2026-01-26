package com.optic.pramosreservasappz.domain.model.fixture

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String
)