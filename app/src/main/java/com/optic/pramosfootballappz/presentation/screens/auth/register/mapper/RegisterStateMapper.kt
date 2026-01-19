package com.optic.pramosfootballappz.presentation.screens.auth.register.mapper

import com.optic.pramosfootballappz.domain.model.User
import com.optic.pramosfootballappz.presentation.screens.auth.register.RegisterState

fun RegisterState.toUser(): User {
    return User(
        username = username,
        email = email,
        password = password
    )
}