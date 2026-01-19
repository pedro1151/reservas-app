package com.optic.pramosfootballappz.presentation.screens.prode.leagueprodefixtures

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.R
import com.optic.pramosfootballappz.domain.model.League.LeagueCompleteResponse
import com.optic.pramosfootballappz.domain.util.Resource

import com.optic.pramosfootballappz.presentation.components.ProgressBar
import com.optic.pramosfootballappz.presentation.components.progressBar.CustomProgressBar
import com.optic.pramosfootballappz.presentation.screens.prode.ProdeViewModel
import com.optic.pramosfootballappz.presentation.screens.prode.topbar.ProdeTopBar
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext

// 👉 Helper fuera del composable
fun LeagueCompleteResponse.getLatestSeasonYear(): Int? {
    return this.seasons
        .maxByOrNull { it.year }  // Busca la season con el año más alto
        ?.year
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LeagueProdeScreen(
    leagueId: Int,
    navController: NavHostController,
    isAuthenticated: Boolean

)
{
    val viewModel: ProdeViewModel = hiltViewModel()

    // para idioma
    val localizedContext = LocalizedContext.current

    val context = LocalContext.current

    val leagueStateSingle by viewModel.leagueStateSingle.collectAsState()
    val leagueFixtureRoundState by viewModel.fixtureLeagueState.collectAsState()

    val isSaving by viewModel.isSaving.collectAsState()

    // Llamar a la función solo una vez al inicio
    LaunchedEffect(leagueId) {
        viewModel.getUserFixturePredictions(leagueId, 2025)
        viewModel.getLeagueById(leagueId)
      //  viewModel.getFixtureByRound(leagueId, 2025, "League Stage - 6")
    }



    Scaffold(
        topBar = {
            ProdeTopBar(
                title = localizedContext.getString(R.string.prode_mispredicciones_screen_title),
                navController = navController,
                isSaving = isSaving,             // <-- 🔥 LO PASAS AQUÍ
                isEditing = viewModel.isEditing.value,
                onEditClick = { viewModel.toggleEditing() },
                onSavingClick = {viewModel.saveAllPredictions(context, isAuthenticated)}
            )
        }
    ) { paddingValues ->
        when (leagueStateSingle) {
            is Resource.Loading -> {
                ProgressBar()
            }

            is Resource.Success -> {
                val data = (leagueStateSingle as Resource.Success).data
                LeagueProdeContent(
                    paddingValues = paddingValues,
                    league = data,
                    navController,
                    viewModel,
                    leagueFixtureState = leagueFixtureRoundState
                )
            }

            is Resource.Failure -> {
                // mostrar error: result.message
            }

            else -> {}
        }

        // 🔥 OVERLAY GLOBAL DE LOADING (solo uno)
        if (isSaving) {
            CustomProgressBar(isLoading = isSaving)
        }

    }
}