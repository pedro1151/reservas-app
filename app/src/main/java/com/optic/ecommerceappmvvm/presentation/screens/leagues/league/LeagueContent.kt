package com.optic.ecommerceappmvvm.presentation.screens.leagues.league

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
import com.optic.ecommerceappmvvm.domain.model.League.League

import com.optic.ecommerceappmvvm.presentation.screens.client.playerStats.components.PlaceholderTab
import com.optic.ecommerceappmvvm.presentation.screens.leagues.league.header.LeagueHeader

import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun LeagueContent(
    paddingValues: PaddingValues,
    league: League,
    navController: NavHostController
) {
    val tabTitles = listOf("Clasificacion", "Partidos", "Noticias", "Estad. Jugador", "Temporadas")
    val pagerState = rememberPagerState(pageCount = { tabTitles.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        LeagueHeader(league, paddingValues)

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
                0 -> PlaceholderTab("Clasificacion")
                1 -> PlaceholderTab("Partidos")
                2 -> PlaceholderTab("Noticias")
                3 -> PlaceholderTab("Estad. Jugador")
                4 -> PlaceholderTab("Temporadas")
            }
        }
    }
}