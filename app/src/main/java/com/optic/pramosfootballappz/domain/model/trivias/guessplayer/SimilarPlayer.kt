package com.optic.pramosfootballappz.domain.model.trivias.guessplayer


import com.google.gson.annotations.SerializedName


data class SimilarPlayer(
    @SerializedName("id")
    val id: Int,

    @SerializedName("firstname")
    val firstname: String,

    @SerializedName("photo")
    val photo: String? = null,

    @SerializedName("nationality")
    val nationality: String? = null,

    @SerializedName("similarity_score")
    val similarityScore: Float
)


