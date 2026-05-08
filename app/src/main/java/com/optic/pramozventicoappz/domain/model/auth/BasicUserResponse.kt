package com.optic.pramozventicoappz.domain.model.auth


import com.google.gson.annotations.SerializedName

data class BasicUserResponse(
    @SerializedName("username") var username: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null
)
