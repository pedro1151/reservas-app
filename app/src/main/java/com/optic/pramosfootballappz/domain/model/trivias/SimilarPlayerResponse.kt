package com.optic.pramosfootballappz.domain.model.trivias


import com.google.gson.annotations.SerializedName

data class SimilarPlayerResponse(
    @SerializedName("players") val players: List<SimilarPlayer>,
    @SerializedName("target_player_id") val   targetPlayerId: Int

)


