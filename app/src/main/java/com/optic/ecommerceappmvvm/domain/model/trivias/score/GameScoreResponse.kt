package com.optic.ecommerceappmvvm.domain.model.trivias.score

import com.google.gson.annotations.SerializedName

data class GameScoreResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("game_id") val gameId: Int,
    @SerializedName("game_code") val gameCode: String,
    @SerializedName("score") val score: Int,
    @SerializedName("dificulty") val dificulty: String,
    @SerializedName("created_by") val createdBy: String?
)
