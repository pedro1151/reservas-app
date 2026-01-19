package com.optic.pramosfootballappz.presentation.navigation.graph.roles

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.optic.pramosfootballappz.presentation.navigation.Graph
import com.optic.pramosfootballappz.presentation.navigation.screen.roles.RolesScreen
import com.optic.pramosfootballappz.presentation.screens.admin.home.AdminHomeScreen
import com.optic.pramosfootballappz.presentation.screens.menubottombar.ClientHomeScreen
import com.optic.pramosfootballappz.presentation.screens.roles.RolesScreen

fun NavGraphBuilder.RolesNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.ROLES,
        startDestination = RolesScreen.Roles.route
    ) {

        composable(route = RolesScreen.Roles.route) {
            RolesScreen(navController)
        }

        composable(route = Graph.CLIENT) {
            ClientHomeScreen()
        }

        composable(route = Graph.ADMIN) {
            AdminHomeScreen()
        }

    }
}