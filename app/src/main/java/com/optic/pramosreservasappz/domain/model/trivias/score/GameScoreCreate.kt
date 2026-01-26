package com.optic.pramosreservasappz.domain.model.trivias.score
import com.google.gson.annotations.SerializedName

data class GameScoreCreate(
    @SerializedName("game_code") val gameCode: String,
    @SerializedName("score") val score: Int,
    @SerializedName("dificulty") val dificulty: String,
    @SerializedName("created_by") val createdBy: String?
)
