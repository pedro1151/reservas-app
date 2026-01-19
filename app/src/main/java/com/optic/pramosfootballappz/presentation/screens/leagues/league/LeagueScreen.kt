package com.optic.pramosfootballappz.presentation.screens.leagues.league

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
import com.optic.pramosfootballappz.domain.model.League.LeagueCompleteResponse
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.presentation.components.BackTopBar

import com.optic.pramosfootballappz.presentation.components.ProgressBar

// 👉 Helper fuera del composable
fun LeagueCompleteResponse.getLatestSeasonYear(): Int? {
    return this.seasons
        .maxByOrNull { it.year }  // Busca la season con el año más alto
        ?.year
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LeagueScreen(
    leagueId: Int,
    navController: NavHostController

)
{
    val viewModel: LeagueViewModel = hiltViewModel()

    val leagueState by viewModel.leagueState.collectAsState()
    val leagueFixtureState by viewModel.fixtureLeagueState.collectAsState()

    // Llamar a la función solo una vez al inicio
    LaunchedEffect(leagueId) {
        viewModel.getLeagueById(leagueId)
        viewModel.getLeagueFixture(leagueId, 2025, 0)

    }


    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController
            )
        }
    ) { paddingValues ->
        when (leagueState) {
            is Resource.Loading -> {
                ProgressBar()
            }

            is Resource.Success -> {
                val data = (leagueState as Resource.Success).data
                LeagueContent(
                    paddingValues = paddingValues,
                    league = data,
                    navController,
                    viewModel,
                    leagueFixtureState = leagueFixtureState
                )
            }

            is Resource.Failure -> {
                // mostrar error: result.message
            }

            else -> {}
        }

    }
}