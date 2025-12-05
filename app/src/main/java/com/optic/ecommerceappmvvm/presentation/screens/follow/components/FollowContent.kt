package com.optic.ecommerceappmvvm.presentation.screens.follow.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.player.Player
import kotlinx.coroutines.launch
import androidx.compose.foundation.pager.HorizontalPager
import com.optic.ecommerceappmvvm.presentation.screens.follow.FollowViewModel
import com.optic.ecommerceappmvvm.presentation.screens.follow.components.followedPlayer.FinalPlayerList
import com.optic.ecommerceappmvvm.presentation.screens.follow.components.followedTeam.FinalTeamList


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FollowContent(
    paddingValues: PaddingValues,
    teams: List<Team>,
    players: List<Player>,
    followedPlayers:List<Player>,
    followedTeams: List<Team>,
    navController: NavHostController,
    onFollowClick: (Int) -> Unit = {},
    onUnFollowClick : (Int) -> Unit = {},

    onFollowTeamClick : (Int) -> Unit = {},
    onUnFollowTeamClick : (Int) -> Unit = {},
    viewModel: FollowViewModel
) {
    val tabTitles = listOf("Equipos", "Jugadores")
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
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
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            //modifier = Modifier.weight(1f) // 👈 LIMITA la altura
        ) { page ->
            when (page) {
                0 -> FinalTeamList(
                    suggestedTeams = teams,
                    followedTeams = followedTeams,
                    navController = navController,
                    onFollowClick = onFollowTeamClick,
                    onUnFollowClick =  onUnFollowTeamClick,
                    paddingValues = PaddingValues(1.dp)
                )
                1 -> FinalPlayerList(
                    players = players,
                    followedPlayers = followedPlayers,
                    navController = navController,
                    onFollowClick = onFollowClick,
                    onUnFollowClick = onUnFollowClick,
                    paddingValues = PaddingValues(1.dp),
                    viewModel = viewModel
                )
            }
        }
    }
}