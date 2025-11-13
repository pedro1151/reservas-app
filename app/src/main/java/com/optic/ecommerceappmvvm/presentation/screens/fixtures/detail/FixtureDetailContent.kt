package com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail

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
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.components.PlaceholderTab
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail.components.FixtureDetailHeader
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail.components.facetoface.VersusFixtureContent
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail.components.lineups.FixtureLineupsScreen

import com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail.components.standings.LeagueStandingsList

import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun FixtureDetailContent(
    paddingValues: PaddingValues,
    fixture: FixtureResponse,
    navController: NavHostController
) {
    val tabTitles = listOf("Alineacion", "Clasificacion",  "Cara a Cara", "Estadisticas")
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        FixtureDetailHeader(fixture, paddingValues)

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            edgePadding = 16.dp
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {

                0 -> {
                    FixtureLineupsScreen(
                        paddingValues = paddingValues,
                        fixtureId = fixture.id
                    )
                }
                1-> {
                    fixture.league?.let { league ->
                        fixture.leagueSeason?.let {
                            LeagueStandingsList(
                                paddingValues = paddingValues,
                                league = league,
                                season = it,
                                teamHome = fixture.teamHome,
                                teamAway = fixture.teamAway
                            )
                        }
                    } ?: PlaceholderTab("Liga no disponible")
                }
                2 -> {
                    VersusFixtureContent(
                        modifier = Modifier,
                        navController = navController,
                        fixture =  fixture,
                        paddingValues = paddingValues
                    )
                }

                3 -> PlaceholderTab("Estadisticas")

            }
        }
    }
}
