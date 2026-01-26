package com.optic.pramosreservasappz.presentation.navigation.graph.client

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

import com.optic.pramosreservasappz.presentation.navigation.Graph
import com.optic.pramosreservasappz.presentation.navigation.graph.profile.ProfileNavGraph
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.auth.login.LoginScreen
import com.optic.pramosreservasappz.presentation.screens.auth.login.LoginViewModel
import com.optic.pramosreservasappz.presentation.screens.auth.login.components.LoginContentPless
import com.optic.pramosreservasappz.presentation.screens.mas.MasScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.*
import androidx.compose.animation.core.tween

import com.optic.pramosreservasappz.presentation.screens.auth.login.basiclogin.BasicLoginScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarScreen
import com.optic.pramosreservasappz.presentation.screens.clients.ClientPrincipalScreen
import com.optic.pramosreservasappz.presentation.screens.services.ServiceScreen

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClientNavGraph(
    navController: NavHostController,
    isAuthenticated: Boolean,
    onShowRewardAd: () -> Unit
) {
    AnimatedNavHost(
        navController = navController,
        route = Graph.CLIENT,
        startDestination = if (isAuthenticated)
            ClientScreen.Clientes.route
        else
            ClientScreen.Login.route
    ) {

        composable(route = ClientScreen.Clientes.route) {
           ClientPrincipalScreen(navController, isAuthenticated)
        }

        composable(route = ClientScreen.Calendario.route) {
            CalendarScreen(navController, isAuthenticated)
        }

        composable(route = ClientScreen.Servicios.route) {
           ServiceScreen(navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.Login.route,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 350)
                ) // entra desde derecha → izquierda
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 350)
                ) // sale hacia derecha ← izquierda
            },
            popEnterTransition = {
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(durationMillis = 350)
                ) // cuando regresa, entra desde izq → der
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(durationMillis = 350)
                ) // cuando retrocede, sale hacia izq ← der
            }
        ) {
            LoginScreen(navController)
        }

        composable(Graph.CLIENT + "/{email}") { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(ClientScreen.Login.route)
            }
            val viewModel: LoginViewModel = hiltViewModel(parentEntry)

            val email = backStackEntry.arguments?.getString("email") ?: ""
            LoginContentPless(navController, viewModel, email)
        }






        composable(
            route = ClientScreen.Profile.route

        ) {

            MasScreen(navController, isAuthenticated)
        }

        composable(
            route = ClientScreen.BasicLogin.route

        ) {

            BasicLoginScreen(navController = navController, )
        }

        composable(route = ClientScreen.Mas.route) {
            MasScreen(navController, isAuthenticated)
        }


        ProfileNavGraph(navController)

    }
}