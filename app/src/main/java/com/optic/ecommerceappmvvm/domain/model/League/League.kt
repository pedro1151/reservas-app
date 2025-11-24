package com.optic.ecommerceappmvvm.domain.model.League

import com.google.gson.annotations.SerializedName
import com.optic.ecommerceappmvvm.domain.model.administracion.Country
import java.io.Serializable

data class League(
    @SerializedName("id") val id: Int,
    @SerializedName("api_id") val apiId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("logo") val logo: String?,
    @SerializedName("country") val country: Country? // 👈 país opcional
) : Serializable