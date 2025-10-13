package com.optic.ecommerceappmvvm.domain.model.trivias.guessplayer

import com.google.gson.annotations.SerializedName

data class GuessPlayerResponse(
    @SerializedName("target")
    val target: GuessPlayer,

    @SerializedName("similars")
    val similars: List<SimilarPlayer>
)



