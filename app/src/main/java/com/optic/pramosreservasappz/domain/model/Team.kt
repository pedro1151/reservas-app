package com.optic.pramosreservasappz.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Team(
    @SerializedName("id") val id: Int? = null,
    //@SerializedName("api_id") val apiId: Int? = null,
    @SerializedName("name") var name: String,
    @SerializedName("country") val country: String? = null,
    //@SerializedName("founded") val founded: Int? = null,
    //@SerializedName("code") val code: String? = null,
    //@SerializedName("national") val national: Boolean? = null,
    @SerializedName("logo") val logo: String? = null,
): Serializable {

    fun toJson(): String = Gson().toJson(Team(
        id,
        //apiId,
        name,
        country,
        //founded,
        //code,
        //national,
        logo
    ))

    companion object {
        fun fromJson(data: String): Team = Gson().fromJson(data, Team::class.java)
    }

}