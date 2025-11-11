package com.optic.ecommerceappmvvm.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.optic.ecommerceappmvvm.presentation.navigation.graph.client.ClientNavGraph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.home.components.ClientBottomBar


@Composable
fun ClientHomeScreen(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Rutas donde se debe mostrar el BottomBar
    val bottomBarRoutes = listOf(
        ClientScreen.Matches.route,
        ClientScreen.Leagues.route,
        ClientScreen.Mas.route,
        ClientScreen.Profile.route,
        ClientScreen.Follow.route,
        ClientScreen.Games.route
    )

    val shouldShowBottomBar = currentDestination?.route in bottomBarRoutes

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        bottomBar = {
            if (shouldShowBottomBar) {
                ClientBottomBar(navController = navController)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
          //  ClientNavGraph(navController = navController,)
        }
    }
}