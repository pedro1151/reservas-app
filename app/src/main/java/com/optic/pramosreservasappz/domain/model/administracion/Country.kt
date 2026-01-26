package com.optic.pramosreservasappz.domain.model.administracion

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Country(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String,
    @SerializedName("flag") val logo: String
) : Serializable