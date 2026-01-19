package com.optic.pramosfootballappz.domain.model.trivias.game.dificulty


import com.google.gson.annotations.SerializedName

data class GameDificulty(

    @SerializedName("code") val code: String,
    @SerializedName("points") val points: Int
)