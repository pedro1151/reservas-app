package com.optic.ecommerceappmvvm.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen

@Composable
fun ClientBottomBar(navController: NavHostController) {

    val screens = listOf(
        ClientScreen.Matches,
        ClientScreen.Games,
        ClientScreen.Leagues,
        ClientScreen.Follow,
        ClientScreen.Profile,
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
            containerColor = MaterialTheme.colorScheme.primaryContainer,
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