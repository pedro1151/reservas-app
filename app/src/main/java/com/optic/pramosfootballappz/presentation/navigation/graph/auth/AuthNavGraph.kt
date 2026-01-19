package com.optic.pramosfootballappz.presentation.navigation.graph.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.optic.pramosfootballappz.presentation.navigation.Graph
import com.optic.pramosfootballappz.presentation.navigation.screen.auth.AuthScreen
import com.optic.pramosfootballappz.presentation.screens.auth.login.LoginScreen
import com.optic.pramosfootballappz.presentation.screens.auth.register.RegisterScreen
import androidx.navigation.navArgument


fun NavGraphBuilder.AuthNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.Login.route
    ) {
        // ✅ Login con argumento opcional "redirect"
        composable(
            route = "${AuthScreen.Login.route}?redirect={redirect}",
            arguments = listOf(
                navArgument("redirect") {
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            LoginScreen(navController)
        }

        composable(route = AuthScreen.Register.route) {
            RegisterScreen(navController)
        }
    }
}
