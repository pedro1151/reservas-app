package com.optic.pramosreservasappz.presentation.navigation.graph.admin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.optic.pramosreservasappz.presentation.navigation.Graph
import com.optic.pramosreservasappz.presentation.navigation.graph.profile.ProfileNavGraph
import com.optic.pramosreservasappz.presentation.navigation.screen.admin.AdminScreen

@Composable
fun AdminNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ADMIN,
        startDestination = AdminScreen.ProductList.route
    ) {

        composable(route = AdminScreen.ProductList.route) {

        }

        composable(route = AdminScreen.CategoryList.route) {

        }

        composable(route = AdminScreen.Profile.route) {

        }
        ProfileNavGraph(navController)

    }
}