package com.optic.pramozventicoappz.domain.model.auth

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user")
    val user: User? = null,

    @SerializedName("business")
    val business: Business? = null,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("refresh_token")
    val refreshToken: String? = null
) {

    // 🔥 Helpers de negocio

    val isOwner: Boolean
        get() = business?.role == "owner"

    val isCollaborator: Boolean
        get() = business?.role == "collaborator"

    val isAdmin: Boolean
        get() = business?.role == "admin"

    val maxCollabs: Int
        get() = business?.plan?.maxCollab ?: 1

    val planCode: String
        get() = business?.plan?.code ?: "basic"

    val planName: String
        get() = business?.plan?.name ?: "Plan Básico"

    val businessName: String
        get() = business?.name ?: "Mi Negocio"

    // 🔥 JSON helpers
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(data: String): AuthResponse =
            Gson().fromJson(data, AuthResponse::class.java)
    }
}

data class Business(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String?,

    @SerializedName("role")
    val role: String,

    @SerializedName("plan")
    val plan: Plan
)

data class Plan(
    @SerializedName("code")
    val code: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("max_collab")
    val maxCollab: Int
)