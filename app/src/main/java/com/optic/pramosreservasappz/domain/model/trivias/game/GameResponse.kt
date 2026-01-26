package com.optic.pramosreservasappz.domain.model.trivias.game

import com.google.gson.annotations.SerializedName

data class GameResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String,
    @SerializedName("category") val category: String?,
    @SerializedName("dificulty") val dificulty: String,
    @SerializedName("logo") val logo: String?,
    @SerializedName("description") val description: String?
)