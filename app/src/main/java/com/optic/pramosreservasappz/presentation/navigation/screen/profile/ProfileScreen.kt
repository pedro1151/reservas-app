package com.optic.pramosreservasappz.presentation.navigation.screen.profile

sealed class ProfileScreen(val route: String) {
    object ProfileUpdate: ProfileScreen("profile/update")
}

