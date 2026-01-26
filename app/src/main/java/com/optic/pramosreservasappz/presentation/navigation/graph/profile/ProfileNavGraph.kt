package com.optic.pramosreservasappz.presentation.navigation.graph.profile

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.optic.pramosreservasappz.presentation.navigation.Graph
import com.optic.pramosreservasappz.presentation.navigation.screen.profile.ProfileScreen
//import com.optic.pramosreservasappz.presentation.screens.profile.update.ProfileUpdateScreen

fun NavGraphBuilder.ProfileNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.PROFILE + "/{user}",
        startDestination = ProfileScreen.ProfileUpdate.route
    ) {

        composable(
            route = ProfileScreen.ProfileUpdate.route,
            arguments = listOf(navArgument("user") {
                type = NavType.StringType
            })
        ) {
            it.arguments?.getString("user")?.let {
                //ProfileUpdateScreen(navController, userParam = it)
            }
        }

    }
}