package com.optic.pramozventicoappz.presentation.screens.menubottombar.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.optic.pramozventicoappz.presentation.navigation.Graph
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen

@Composable
fun ClientBottomBar(navController: NavHostController) {

    val screens = listOf(
        ClientScreen.Sales,
        ClientScreen.Historial,
        ClientScreen.SaleStats,
        ClientScreen.Productos,
        ClientScreen.Mas
    )

    val navBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackEntry?.destination

    val bottomBarDestination = when {
        screens.any { it.route == currentDestination?.route } -> true
        currentDestination?.route?.startsWith(Graph.PLAYER) == true -> true
        else -> false
    }

    if (bottomBarDestination) {
        NavigationBar (
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 0.dp
        ){
            screens.forEach { screen ->
                ClientBottomBarItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }

}