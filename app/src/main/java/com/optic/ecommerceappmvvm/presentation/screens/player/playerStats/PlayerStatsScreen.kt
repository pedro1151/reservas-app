package com.optic.ecommerceappmvvm.presentation.screens.player.playerStats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.components.BackTopBar

import com.optic.ecommerceappmvvm.presentation.components.ProgressBar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerStatsScreen(
    playerId: Int,
    navController: NavHostController

    )
{
    val viewModel: PlayerStatsViewModel = hiltViewModel()

    // stadisticas del jugador
    val state by viewModel.playerStatsState.collectAsState()

    // trauectoria de equipos en los que jugo
    val playerTeamsState by viewModel.playerTeamsState.collectAsState()

    //ultimo equipo
    val playerLastTeamState by viewModel.playerLastTeamState.collectAsState()

    // fixture del ultimo equipo, o equpo vigente
    val fixtureTeamState by viewModel.fixtureTeamsState.collectAsState()

    // Llamar a la función solo una vez al inicio
    LaunchedEffect(playerId) {
        viewModel.getPlayerStats(playerId)
        viewModel.getPlayerTeams(playerId)
        viewModel.getPlayerLastTeam(playerId)
    }
    LaunchedEffect(playerLastTeamState) {
        if (playerLastTeamState is Resource.Success) {
            val lastTeam = (playerLastTeamState as Resource.Success).data
            lastTeam.lastTeam.id?.let { viewModel.getFixtureTeam(it) }
        }
    }


    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController
            )
        }
    ) { paddingValues ->
        when (state) {
            is Resource.Loading -> {
                ProgressBar()
            }

            is Resource.Success -> {
                val data = (state as Resource.Success).data
                PlayerStatsContent(paddingValues, data, navController, playerTeamsState , fixtureTeamState,playerLastTeamState  )
            }

            is Resource.Failure -> {
                // mostrar error: result.message
            }

            else -> {}
        }

    }
}