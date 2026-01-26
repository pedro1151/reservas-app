package com.optic.pramosreservasappz.domain.model.auth


data class LoginPlessRequest(
    val email: String,
    val code: String
)