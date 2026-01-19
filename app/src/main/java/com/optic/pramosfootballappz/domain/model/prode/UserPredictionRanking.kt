package com.optic.pramosfootballappz.domain.model.prode

import com.google.gson.annotations.SerializedName

data class UserPredictionRanking(

    @SerializedName("ranking") val ranking: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("points_awarded") val pointsAwarded: Int


)
