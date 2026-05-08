package com.optic.pramozventicoappz.domain.model.auth


data class LoginPlessRequest(
    val email: String,
    val code: String
)