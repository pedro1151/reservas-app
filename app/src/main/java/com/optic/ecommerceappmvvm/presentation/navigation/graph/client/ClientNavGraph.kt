package com.optic.ecommerceappmvvm.presentation.navigation.graph.client

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.optic.ecommerceappmvvm.domain.model.trivias.guessplayer.GuessPlayer
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.navigation.graph.profile.ProfileNavGraph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.auth.login.LoginScreen
import com.optic.ecommerceappmvvm.presentation.screens.auth.login.LoginViewModel
import com.optic.ecommerceappmvvm.presentation.screens.auth.login.components.LoginContentPless
import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.PlayerStatsScreen
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail.FixtureDetailScreen
import com.optic.ecommerceappmvvm.presentation.screens.follow.FollowScreen
import com.optic.ecommerceappmvvm.presentation.screens.games.GamesPrincipalScreen
import com.optic.ecommerceappmvvm.presentation.screens.games.choicedificulty.ChoiceDificultyScreen
import com.optic.ecommerceappmvvm.presentation.screens.games.galery.guessplayer.PrincipalGuessPlayerScreen
import com.optic.ecommerceappmvvm.presentation.screens.games.galery.guessplayer.PrincipalGuessPlayerVM
import com.optic.ecommerceappmvvm.presentation.screens.games.galery.guessplayer.components.GuessPlayerWinScreen
import com.optic.ecommerceappmvvm.presentation.screens.leagues.league.LeagueScreen
import com.optic.ecommerceappmvvm.presentation.screens.leagues.principal.LeaguePrincipalScreen
import com.optic.ecommerceappmvvm.presentation.screens.mas.MasScreen
import com.optic.ecommerceappmvvm.presentation.screens.matches.MatchesScreen
import com.optic.ecommerceappmvvm.presentation.screens.matches.calendar.CalendarScreen
import com.optic.ecommerceappmvvm.presentation.screens.team.TeamScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.optic.ecommerceappmvvm.presentation.screens.auth.login.basiclogin.BasicLoginScreen
import com.optic.ecommerceappmvvm.presentation.screens.prode.ProdeScreen
import com.optic.ecommerceappmvvm.presentation.screens.prode.ProdeViewModel
import com.optic.ecommerceappmvvm.presentation.screens.prode.leagueprodefixtures.LeagueProdeScreen
import com.optic.ecommerceappmvvm.presentation.screens.prode.ranking.ProdeRankingScreen
import com.optic.ecommerceappmvvm.presentation.screens.prode.ranking.UserPredictionSummaryScreen

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
        startDestination = ClientScreen.Matches.route
    ) {

        composable(route = ClientScreen.Matches.route) {
            MatchesScreen(navController, isAuthenticated, onShowRewardAd)
        }

        composable(route = ClientScreen.Follow.route) {
            FollowScreen(navController, isAuthenticated)
        }

        composable(route = ClientScreen.Prode.route) {
            ProdeScreen(navController, isAuthenticated)
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

        composable(route = ClientScreen.ProdeRanking.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(ClientScreen.Prode.route)
            }
            val viewModel: ProdeViewModel = hiltViewModel(parentEntry)
            ProdeRankingScreen(navController = navController, vm= viewModel)

        }

        composable(
            route = Graph.USER_PREDICTION + "/{userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val userId = backStackEntry.arguments?.getInt("userId") ?: -1

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graph.CLIENT)
            }
            val viewModel: ProdeViewModel = hiltViewModel(parentEntry)

            UserPredictionSummaryScreen(
                navController = navController,
                vm = viewModel,
                userId = userId
            )
        }


        composable(route = ClientScreen.Leagues.route) {
            LeaguePrincipalScreen(navController, isAuthenticated)
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
        composable(route = ClientScreen.Games.route) {
            GamesPrincipalScreen(navController)
        }

        composable(route = Graph.PLAYER + "/{playerId}"
        ) { backStackEntry ->
            val playerId = backStackEntry.arguments?.getString("playerId")?.toInt() ?: 0
            PlayerStatsScreen(navController = navController, playerId = playerId)
        }

        composable(route = Graph.TEAM + "/{teamId}"
        ) { backStackEntry ->
            val teamId = backStackEntry.arguments?.getString("teamId")?.toInt() ?: 0
            TeamScreen(navController = navController, teamId = teamId)
        }
        // DETALLE DE FIXTURE
        composable(route = Graph.FIXTURE + "/{fixtureId}"
        ) { backStackEntry ->
            val fixtureId= backStackEntry.arguments?.getString("fixtureId")?.toInt() ?: 0
            FixtureDetailScreen(navController = navController, fixtureId = fixtureId)
        }

        // DETALLE DE UNA LIGA
        composable(route = Graph.LEAGUE + "/{leagueId}"
        ) { backStackEntry ->
            val leagueId= backStackEntry.arguments?.getString("leagueId")?.toInt() ?: 0
            LeagueScreen(leagueId = leagueId, navController = navController)
        }

        //games

        composable(route = Graph.GAME + "/{gameCode}"
        ) { backStackEntry ->
            val gameCode = backStackEntry.arguments?.getString("gameCode")?.toString() ?: "ADIVJUG"
            if ( gameCode == "ADIVJUG" ){
                // El ViewModel se asocia a esta ruta base
                val viewModel: PrincipalGuessPlayerVM = hiltViewModel(backStackEntry)
                PrincipalGuessPlayerScreen(navController, viewModel, gameCode)
            }


        }
        composable(route = Graph.GAME_DIFICULTY + "/{gameCode}"
        ) { backStackEntry ->
            val gameCode = backStackEntry.arguments?.getString("gameCode")?.toString() ?: "ADIVJUG"
            ChoiceDificultyScreen(navController = navController, gameCode = gameCode)



        }

        composable(
            route = Graph.GUESSPLAYER_WIN + "/{gameCode}/{playerJson}"
        ) { backStackEntry ->

            val gameCode = backStackEntry.arguments?.getString("gameCode") ?: ""
            val playerJson = backStackEntry.arguments?.getString("playerJson")
            val player = Gson().fromJson(playerJson, GuessPlayer::class.java)

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graph.GAME + "/{gameCode}")
            }
            val viewModel: PrincipalGuessPlayerVM = hiltViewModel(parentEntry)

            GuessPlayerWinScreen(
                player = player,
                navController = navController,
                viewModel = viewModel,
                gameCode = gameCode,
            )
        }

        composable(
            route = ClientScreen.Calendar.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = 350)
                ) // entra desde derecha → izquierda
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(durationMillis = 350)
                ) // sale hacia derecha ← izquierda
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(durationMillis = 350)
                ) // cuando regresa, entra desde izq → der
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis = 350)
                ) // cuando retrocede, sale hacia izq ← der
            }
        ) {
            CalendarScreen(
                onDateSelected = { date ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_date", date)

                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Graph.PRODE + "/{leagueId}"
        ) { backStackEntry ->
            val leagueId= backStackEntry.arguments?.getString("leagueId")?.toInt() ?: 0
            LeagueProdeScreen(leagueId = leagueId, navController = navController, isAuthenticated)
        }


        ProfileNavGraph(navController)

    }
}