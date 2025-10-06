package com.optic.ecommerceappmvvm.domain.model.trivias.game.dificulty


import com.google.gson.annotations.SerializedName

data class GameDificulty(

    @SerializedName("code") val code: String,
    @SerializedName("points") val points: Int
)