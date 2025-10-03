package com.optic.ecommerceappmvvm.presentation.screens.games

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
import com.optic.ecommerceappmvvm.domain.model.trivias.game.GameResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.screens.games.list.GameList
import com.optic.ecommerceappmvvm.presentation.screens.leagues.league.LeagueViewModel

import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.components.PlaceholderTab
import com.optic.ecommerceappmvvm.presentation.screens.leagues.league.header.LeagueHeader
import com.optic.ecommerceappmvvm.presentation.screens.leagues.league.leaguematches.LeagueFixture
import com.optic.ecommerceappmvvm.presentation.screens.leagues.league.standings.LeagueStandingsList

import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GamesPrincipalContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    gameState: Resource<List<GameResponse>>
) {
    val tabTitles = listOf("Categoria 1", "Categoria 2", "Categoria 3")
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
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
                0 -> GameList(
                    games = gameState,
                    navController = navController
                )
                1 -> PlaceholderTab("Categoria 2")
                2 -> PlaceholderTab("Categoria 3")
            }
        }
    }
}