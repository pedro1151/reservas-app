package com.optic.pramosfootballappz.presentation.screens.follow


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.presentation.components.PrimaryTopBar
import com.optic.pramosfootballappz.presentation.components.progressBar.CustomProgressBar
import com.optic.pramosfootballappz.presentation.screens.follow.components.FollowContent
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext
import com.optic.pramosfootballappz.R
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FollowScreen(
    navController: NavHostController,
    isAuthenticated: Boolean
) {

    // para idioma
    val localizedContext = LocalizedContext.current


    val tabTitles = listOf(
        localizedContext.getString(R.string.siguiendo_screen_equipos_title),
        localizedContext.getString(R.string.siguiendo_screen_jugadores_title)
    )
    val pagerState = rememberPagerState(pageCount = { tabTitles.size }) // fuera de FollowContent

    val backStackEntry = navController.currentBackStackEntryAsState().value
    val viewModel: FollowViewModel = hiltViewModel()

    val teamsState by viewModel.teamsState.collectAsState()
    val playersState by viewModel.playersState.collectAsState()

    val followedPlayerListState by viewModel.followedPlayerListState.collectAsState()
    val filteredPlayers by viewModel.filteredPlayersState.collectAsState()

    val followedTeamListState by viewModel.followedTeamListState.collectAsState()
    val filteredTeams by viewModel.filteredTeamsState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.start()
    }

    Scaffold(
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = localizedContext.getString(R.string.siguiendo_screen_title)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
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
                    followedPlayers = followedPlayers ,
                    followedTeams = followedTeams ,
                    navController = navController,

                    onFollowClick = { playerId ->
                            viewModel.createFollowedPlayer(playerId, isAuthenticated)
                    },
                    onUnFollowClick = { playerId ->
                            viewModel.deleteFollowedPlayer(playerId, isAuthenticated)
                    },
                    onFollowTeamClick = { teamId ->
                            viewModel.createFollowedTeam(teamId)
                    },
                    onUnFollowTeamClick = { teamId ->
                            viewModel.deleteFollowedTeam(teamId)
                    },
                    viewModel = viewModel,
                    pagerState = pagerState,
                    tabTitles = tabTitles


                )
            }

            teamsState is Resource.Failure || playersState is Resource.Failure -> {
                Text("No estás siguiendo a ningún jugador o equipo.")
            }
        }
        CustomProgressBar(isLoading = teamsState is Resource.Loading && playersState is Resource.Loading)
    }
}