package com.optic.pramosreservasappz.domain.model.auth

data class LoginRequest(
    val email: String,
    val password: String
)