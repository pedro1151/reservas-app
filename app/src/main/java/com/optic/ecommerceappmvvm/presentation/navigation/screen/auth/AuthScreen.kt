package com.optic.ecommerceappmvvm.presentation.navigation.screen.auth

sealed class AuthScreen(val route: String) {

    object Login: AuthScreen("login")
    object Register: AuthScreen("register")
    object LoginPless: AuthScreen("loginPless")

}

