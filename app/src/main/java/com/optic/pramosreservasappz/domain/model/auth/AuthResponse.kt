package com.optic.pramosreservasappz.domain.model.auth

import com.google.gson.Gson

data class AuthResponse(
    val user: User? = null,
    val token: String? = null,
    val refresh_token: String? = null
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(data: String): AuthResponse = Gson().fromJson(data, AuthResponse::class.java)
    }
}
