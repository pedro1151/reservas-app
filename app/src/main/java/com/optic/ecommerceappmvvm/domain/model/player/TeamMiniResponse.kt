package com.optic.ecommerceappmvvm.domain.model.player

import com.google.gson.annotations.SerializedName

data class TeamMiniResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("code") val code: String?,
    @SerializedName("logo") val logo: String?
)