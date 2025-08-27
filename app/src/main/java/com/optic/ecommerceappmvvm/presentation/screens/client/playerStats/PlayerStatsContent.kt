package com.optic.ecommerceappmvvm.presentation.screens.client.playerStats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerWithStats

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.R
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.ecommerceappmvvm.domain.util.Resource

import com.optic.ecommerceappmvvm.presentation.screens.client.playerStats.components.PlayerHeader
import com.optic.ecommerceappmvvm.presentation.screens.client.playerStats.profile.PlayerProfileTab
import com.optic.ecommerceappmvvm.presentation.screens.client.playerStats.statistics.PlayerStatsContentTab
import com.optic.ecommerceappmvvm.presentation.screens.client.playerStats.matches.PlayerMatchesTab
import com.optic.ecommerceappmvvm.presentation.screens.client.playerStats.trayectoria.PlayerTrayectoryTab
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun PlayerStatsContent(
    paddingValues: PaddingValues,
    playerStats: PlayerWithStats,
    navController: NavHostController,
    playerTeamsState: Resource<PlayerTeamsResponse>,
    fixtureTeamState: Resource<List<FixtureResponse>>,
    lastTeam : Resource<PlayerLastTeamResponse>
) {
    val tabTitles = listOf("Perfil", "Estadísticas", "Partidos", "Trayectoria")
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        PlayerHeader(playerStats, paddingValues, lastTeam)

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            edgePadding = 16.dp
        ){
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch{
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
                    PlayerProfileTab(playerStats)
                }
                1 -> {
                    PlayerStatsContentTab(paddingValues = PaddingValues(8.dp), playerStats = playerStats)
                }
                2 -> {
                    PlayerMatchesTab(
                        fixtureState = fixtureTeamState,
                        navController = navController
                    )
                }
                3 -> {
                        PlayerTrayectoryTab(
                            paddingValues = paddingValues,
                            state = playerTeamsState
                        )
                }
            }
        }
    }
}




