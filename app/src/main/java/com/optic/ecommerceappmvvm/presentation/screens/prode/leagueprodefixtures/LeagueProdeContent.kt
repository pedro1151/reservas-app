package com.optic.ecommerceappmvvm.presentation.screens.prode.leagueprodefixtures

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.util.Resource

import com.optic.ecommerceappmvvm.presentation.screens.prode.ProdeViewModel
import com.optic.ecommerceappmvvm.presentation.screens.prode.leagueprodefixtures.header.ProdeHeader


@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun LeagueProdeContent(
    paddingValues: PaddingValues,
    league: LeagueCompleteResponse,
    navController: NavHostController,
    viewModel: ProdeViewModel,
    leagueFixtureState: Resource<List<FixtureResponse>>,
    isSaving:Boolean = false
) {

    val currentRound = remember(league.rounds) {
        league.rounds.firstOrNull { it.isCurrent == true }
    }

    var selectedRound by remember {
        mutableStateOf(currentRound)
    }

    // 🔥 Cuando cambia el round → pedir fixtures
    LaunchedEffect(selectedRound) {
        selectedRound?.let { round ->
            viewModel.getFixtureByRound(
                leagueId = league.id!!,
                season = viewModel.latestSeason,
                round = round.roundName
            )
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        ProdeHeader(
            league,
            paddingValues,
            onRoundSelected = { round ->
                selectedRound = round
            }
        )


        ProdeFixtureList(
            modifier = Modifier,
            navController = navController,
            fixtureState  = leagueFixtureState,
            vm = viewModel
        )



    }
}