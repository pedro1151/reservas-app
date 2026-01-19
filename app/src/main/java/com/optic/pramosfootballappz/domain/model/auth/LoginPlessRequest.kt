package com.optic.pramosfootballappz.domain.model.auth


data class LoginPlessRequest(
    val email: String,
    val code: String
)