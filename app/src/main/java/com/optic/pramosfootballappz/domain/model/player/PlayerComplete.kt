package com.optic.pramosfootballappz.domain.model.player


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlayerComplete(
    @SerializedName("id") val id: Int,
    @SerializedName("api_id") val apiId: Int,
    @SerializedName("name") var name: String,
    @SerializedName("firstname") var firstname: String?,
    @SerializedName("lastname") var lastname: String?,
    @SerializedName("age") var age: String?,
    @SerializedName("nationality") var nationality: String?,
    @SerializedName("birth_date") var birthDate : String?,
    @SerializedName("birth_place") var birthPlace: String?,
    @SerializedName("birth_country") var birthCountry: String?,
    @SerializedName("height") var height: String?,
    @SerializedName("weight") var weight: String?,
    @SerializedName("photo") var photo: String?,
    @SerializedName("injured") var injured: Boolean,
    @SerializedName("last_team") var lastTeam : TeamMiniResponse?
): Serializable