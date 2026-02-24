package com.optic.pramosreservasappz.presentation.screens.auth.register.mapper

import com.optic.pramosreservasappz.domain.model.auth.User
import com.optic.pramosreservasappz.presentation.screens.auth.register.RegisterState

fun RegisterState.toUser(): User {
    return User(
        username = username,
        email = email,
        password = password
    )
}