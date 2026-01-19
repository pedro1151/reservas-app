package com.optic.pramosfootballappz.presentation.screens.player.playerStats

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.optic.pramosfootballappz.domain.model.player.stats.PlayerWithStats

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.R
import com.optic.pramosfootballappz.domain.model.fixture.FixtureResponse
import com.optic.pramosfootballappz.domain.model.player.PlayerComplete
import com.optic.pramosfootballappz.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.pramosfootballappz.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.pramosfootballappz.domain.util.Resource

import com.optic.pramosfootballappz.presentation.screens.player.playerStats.components.PlayerHeader
import com.optic.pramosfootballappz.presentation.screens.player.playerStats.profile.PlayerProfileTab
import com.optic.pramosfootballappz.presentation.screens.player.playerStats.statistics.PlayerStatsContentTab
import com.optic.pramosfootballappz.presentation.screens.player.playerStats.matches.PlayerMatchesTab
import com.optic.pramosfootballappz.presentation.screens.player.playerStats.trayectoria.PlayerTrayectoryTab
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext
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




