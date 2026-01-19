package com.optic.ecommerceappmvvm.presentation.screens.team


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
import com.optic.ecommerceappmvvm.R
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.team.TeamResponse
import com.optic.ecommerceappmvvm.domain.util.Resource

import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.components.PlaceholderTab
import com.optic.ecommerceappmvvm.presentation.screens.team.components.TeamHeader
import com.optic.ecommerceappmvvm.presentation.screens.team.components.resume.ResumeContent
import com.optic.ecommerceappmvvm.presentation.screens.team.components.stats.TeamStatsScreen
import com.optic.ecommerceappmvvm.presentation.screens.team.components.teamFixture.TeamFixture
import com.optic.ecommerceappmvvm.presentation.settings.idiomas.LocalizedContext
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun TeamContent(
    paddingValues: PaddingValues,
    team: TeamResponse,
    fixtureState: Resource<List<FixtureResponse>>,
    nexFixtureState: Resource<FixtureResponse>,
    topFiveFixtureState: Resource<List<FixtureResponse>>,
    navController: NavHostController
) {

    // para idioma
    val localizedContext = LocalizedContext.current

    val tabTitles = listOf(
        localizedContext.getString(R.string.team_detail_screen_option_resumen_title),
        localizedContext.getString(R.string.team_detail_screen_option_partidos_title),
        localizedContext.getString(R.string.team_detail_screen_option_estadisticas_title)
    )
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        TeamHeader(team, paddingValues)

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
                0 -> team?.id?.let {
                    ResumeContent(
                        paddingValues = paddingValues,
                        nextFixtureState = nexFixtureState,
                        topFiveFixtureState = topFiveFixtureState,
                        teamId = it,
                        navController = navController,
                        team = team
                    )
                }
                1 -> TeamFixture(
                    modifier = Modifier,
                    navController = navController,
                    fixtureState = fixtureState,
                    title = "Partidos",
                    paddingValues = paddingValues
                )
                2 -> team?.id?.let {
                    TeamStatsScreen(
                        paddingValues = paddingValues,
                        season = 2023,
                        teamId = it,
                        date = ""
                    )
                }
            }
        }
    }
}
