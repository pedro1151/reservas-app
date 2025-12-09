package com.optic.ecommerceappmvvm.presentation.screens.prode.leaguefixtures

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.League.LeagueCompleteResponse
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.util.Resource

import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.components.PlaceholderTab
import com.optic.ecommerceappmvvm.presentation.screens.leagues.league.header.LeagueHeader
import com.optic.ecommerceappmvvm.presentation.screens.leagues.league.leaguematches.LeagueFixture
import com.optic.ecommerceappmvvm.presentation.screens.leagues.league.standings.LeagueStandingsList
import com.optic.ecommerceappmvvm.presentation.screens.prode.ProdeViewModel

import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun LeagueProdeContent(
    paddingValues: PaddingValues,
    league: LeagueCompleteResponse,
    navController: NavHostController,
    viewModel: ProdeViewModel,
    leagueFixtureState: Resource<List<FixtureResponse>>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        LeagueHeader(league, paddingValues)

        LeagueFixture(
            modifier = Modifier,
            navController = navController,
            fixtureState  = leagueFixtureState
        )



    }
}