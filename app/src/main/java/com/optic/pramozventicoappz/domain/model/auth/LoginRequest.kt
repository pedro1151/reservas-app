package com.optic.pramozventicoappz.domain.model.auth

data class LoginRequest(
    val email: String,
    val password: String
)