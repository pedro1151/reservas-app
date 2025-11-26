package com.optic.ecommerceappmvvm.presentation.screens.team

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
fun TeamScreen(
    teamId: Int,
    navController: NavHostController

    )
{
    val viewModel: TeamViewModel = hiltViewModel()

    val state by viewModel.teamState.collectAsState()
    val fixtureState by viewModel.fixtureTeamsState.collectAsState()
    val nextFixtureState by viewModel.nextFixtureTeamsState.collectAsState()
    val topFiveFixtureState by viewModel.topFiveFixtureTeamsState.collectAsState()

    // Llamar a la función solo una vez al inicio
    LaunchedEffect(teamId) {
        viewModel.getTeamById  (teamId)
        // Llamamos la función cuando entra la pantalla
        viewModel.getFixtureTeam (teamId)
        viewModel.getTopFiveFixtureTeam(teamId)
        viewModel.getNextFixtureTeam(teamId)
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
                TeamContent(
                    paddingValues = paddingValues,
                    team = data,
                    fixtureState = fixtureState,
                    nexFixtureState = nextFixtureState,
                    topFiveFixtureState = topFiveFixtureState,
                    navController
                )
            }

            is Resource.Failure -> {
                // mostrar error: result.message
            }

            else -> {}
        }

    }
}