package com.optic.pramosreservasappz.domain.model.trivias

import com.google.gson.annotations.SerializedName

data class SimilarPlayer(
    @SerializedName("id") val id: Int,
    @SerializedName("fisrtname") val firstName: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("team") val team: String?,
    @SerializedName("position") val position: String?,
    @SerializedName("nacionality") val nacionality: String?

)
