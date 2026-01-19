package com.optic.pramosfootballappz.domain.model

import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


data class Rol(
    val id: String,
    val name: String,
    val image: String,
    val route: String,
    val codigo: String
) {
    fun toJson(): String = Gson().toJson(Rol(
        id,
        name,
        if (!image.isNullOrBlank()) URLEncoder.encode(image, StandardCharsets.UTF_8.toString()) else "",
        route,
        codigo
    ))

    companion object {
        fun fromJson(data: String): Rol = Gson().fromJson(data, Rol::class.java)
    }
}
