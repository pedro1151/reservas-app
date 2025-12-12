package com.optic.ecommerceappmvvm.presentation.screens.prode.leaguefixtures

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
import com.optic.ecommerceappmvvm.presentation.screens.prode.leaguefixtures.header.ProdeHeader


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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        ProdeHeader(
            league,
            paddingValues
        )

        ProdeFixtureList(
            modifier = Modifier,
            navController = navController,
            fixtureState  = leagueFixtureState,
            vm = viewModel
        )



    }
}