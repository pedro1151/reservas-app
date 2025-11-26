package com.optic.ecommerceappmvvm.presentation.screens.follow


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.components.PrimaryTopBar
import com.optic.ecommerceappmvvm.presentation.components.ProgressBar
import com.optic.ecommerceappmvvm.presentation.components.progressBar.CustomProgressBar
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.auth.AuthScreen
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.follow.components.FollowContent
import com.optic.ecommerceappmvvm.presentation.ui.theme.GreyLight

@Composable
fun FollowScreen(
    navController: NavHostController,
    isAuthenticated: Boolean
) {

    val backStackEntry = navController.currentBackStackEntryAsState().value
    val viewModel: FollowViewModel = hiltViewModel()

    val teamsState by viewModel.teamsState.collectAsState()
    val playersState by viewModel.playersState.collectAsState()

    val followedPlayerListState by viewModel.followedPlayerListState.collectAsState()
    val filteredPlayers by viewModel.filteredPlayersState.collectAsState()

    val followedTeamListState by viewModel.followedTeamListState.collectAsState()
    val filteredTeams by viewModel.filteredTeamsState.collectAsState()

    Scaffold(
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = "Siguiendo"
            )
        },
        containerColor = GreyLight
    ) { paddingValues ->



        when {
            teamsState is Resource.Loading || playersState is Resource.Loading -> {
               /*
                Box(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                */

            }

            teamsState is Resource.Success && playersState is Resource.Success -> {
                val followedPlayers =
                    (followedPlayerListState as? Resource.Success)?.data ?: emptyList()
                val followedTeams =
                    (followedTeamListState as? Resource.Success)?.data ?: emptyList()

                FollowContent(
                    paddingValues = paddingValues,
                    teams = filteredTeams,
                    players = filteredPlayers,
                    followedPlayers = if (isAuthenticated) followedPlayers else emptyList(),
                    followedTeams = if (isAuthenticated) followedTeams else emptyList(),
                    navController = navController,

                    onFollowClick = { playerId ->
                        if (isAuthenticated) {
                            viewModel.createFollowedPlayer(playerId)
                        } else {
                            navController.navigate(ClientScreen.Login.route)
                        }
                    },
                    onUnFollowClick = { playerId ->
                        if (isAuthenticated) {
                            viewModel.deleteFollowedPlayer(playerId)
                        } else {
                            navController.navigate(ClientScreen.Login.route)
                        }
                    },
                    onFollowTeamClick = { teamId ->
                        if (isAuthenticated) {
                            viewModel.createFollowedTeam(teamId)
                        } else {
                            navController.navigate(ClientScreen.Login.route)

                        }
                    },
                    onUnFollowTeamClick = { teamId ->
                        if (isAuthenticated) {
                            viewModel.deleteFollowedTeam(teamId)
                        } else {
                            navController.navigate(ClientScreen.Login.route)

                        }
                    }


                )
            }

            teamsState is Resource.Failure || playersState is Resource.Failure -> {
                Text("No estás siguiendo a ningún jugador o equipo.")
            }
        }
        CustomProgressBar(isLoading = teamsState is Resource.Loading && playersState is Resource.Loading)
    }
}