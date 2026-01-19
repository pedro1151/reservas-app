package com.optic.pramosfootballappz.presentation.screens.profile.update.mapper

import com.optic.pramosfootballappz.domain.model.User
import com.optic.pramosfootballappz.presentation.screens.profile.update.ProfileUpdateState

fun ProfileUpdateState.toUser(): User {
    return User(
        username = name
    )
}