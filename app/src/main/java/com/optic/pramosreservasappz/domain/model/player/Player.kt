package com.optic.pramosreservasappz.domain.model.player

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Player(
    @SerializedName("id") val id: Int? = null,
    //@SerializedName("api_id") val apiId: Int? = null,
    @SerializedName("name") var name: String?,
    @SerializedName("firstname") var firstname: String?,
    @SerializedName("lastname") var lastname: String?,
    //@SerializedName("age") var age: String?,
    //@SerializedName("nationality") var nationality: String?,
    //@SerializedName("birth_date") var birthDate : String?,
    //@SerializedName("birth_place") var birthPlace: String?,
    //@SerializedName("birth_country") var birthCountry: String?,
    //@SerializedName("height") var height: String?,
    //@SerializedName("weight") var weight: String?,
    @SerializedName("photo") var photo: String?,
    @SerializedName("last_team") var lastTeam : TeamMiniResponse?
): Serializable