package com.optic.ecommerceappmvvm.domain.model.auth


data class LoginPlessRequest(
    val email: String,
    val code: String
)