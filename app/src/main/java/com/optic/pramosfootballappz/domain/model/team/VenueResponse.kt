package com.optic.pramosfootballappz.domain.model.team

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VenueResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("api_id") val apiId: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("capacity") val capacity: Int? = null,
    @SerializedName("surface") val surface: String? = null,
    @SerializedName("image") val image: String? = null
) : Serializable