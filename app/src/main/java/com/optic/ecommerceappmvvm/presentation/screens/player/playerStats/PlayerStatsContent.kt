package com.optic.ecommerceappmvvm.presentation.screens.player.playerStats

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
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.R
import com.optic.ecommerceappmvvm.domain.model.fixture.FixtureResponse
import com.optic.ecommerceappmvvm.domain.model.player.PlayerComplete
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.ecommerceappmvvm.domain.util.Resource

import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.components.PlayerHeader
import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.profile.PlayerProfileTab
import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.statistics.PlayerStatsContentTab
import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.matches.PlayerMatchesTab
import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.trayectoria.PlayerTrayectoryTab
import com.optic.ecommerceappmvvm.presentation.settings.idiomas.LocalizedContext
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
    lastTeam : Resource<PlayerLastTeamResponse>,
    playerComplete: Resource<PlayerComplete>
) {

    // para idioma
    val localizedContext = LocalizedContext.current

    val tabTitles = listOf(
        localizedContext.getString(R.string.jugador_detail_screen_option_perfil_title),
        localizedContext.getString(R.string.jugador_detail_screen_option_estadisticas_title),
        localizedContext.getString(R.string.jugador_detail_screen_option_partidos_title),
        localizedContext.getString(R.string.jugador_detail_screen_option_trayectoria_title)
    )
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        PlayerHeader(playerComplete)

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
                    PlayerProfileTab(playerComplete)
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




