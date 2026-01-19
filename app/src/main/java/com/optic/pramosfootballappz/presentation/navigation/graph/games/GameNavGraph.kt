package com.optic.pramosfootballappz.presentation.navigation.graph.games

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.optic.pramosfootballappz.presentation.navigation.Graph
import com.optic.pramosfootballappz.presentation.navigation.screen.games.GameScreen
import com.optic.pramosfootballappz.presentation.screens.games.GamesPrincipalScreen
import com.optic.pramosfootballappz.presentation.screens.games.choicedificulty.ChoiceDificultyScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GameNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.GAME,
        startDestination = GameScreen.Games.route
    ) {

        composable(route = GameScreen.Games.route) {
            GamesPrincipalScreen(navController)
        }

        composable(route = Graph.GAME + "/{gameCode}"
        ) { backStackEntry ->
            val gameCode = backStackEntry.arguments?.getString("gameCode")?.toString() ?: "ADIVJUG"
            ChoiceDificultyScreen(navController = navController, gameCode = gameCode)
        }

    }
}