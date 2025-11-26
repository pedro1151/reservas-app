package com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail.stats


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.optic.ecommerceappmvvm.domain.model.fixture.stats.FixtureStatisticItem
import com.optic.ecommerceappmvvm.domain.model.fixture.stats.FixtureStatsResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.detail.FixtureDetailViewModel

@Composable
fun FixtureStatsScreen(
    paddingValues: PaddingValues,
    fixtureId: Int
) {
    val viewModel: FixtureDetailViewModel = hiltViewModel()
    val statsState by viewModel.fixtureStatsState.collectAsState()

    LaunchedEffect(fixtureId) {
        viewModel.getFixtureStats(fixtureId)
    }

    when (statsState) {
        is Resource.Loading -> Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        is Resource.Failure -> Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (statsState as Resource.Failure).message
                    ?: "Error al cargar estadísticas",
                color = MaterialTheme.colorScheme.error
            )
        }

        is Resource.Success -> {
            val stats = (statsState as Resource.Success).data?.response ?: emptyList()
            if (stats.size < 2) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay estadísticas disponibles")
                }
                return
            }

            val home = stats[0]
            val away = stats[1]

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TeamsHeader(home.team.logo, home.team.name, away.team.logo, away.team.name)
                }

                home.statistics.forEachIndexed { index, stat ->
                    val awayStat = away.statistics[index]
                    item {
                        StatRow(
                            homeStat = stat,
                            awayStat = awayStat
                        )
                    }
                }
            }
        }

        else -> {}
    }
}

@Composable
fun TeamsHeader(
    homeLogo: String?,
    homeName: String,
    awayLogo: String?,
    awayName: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamColumn(logo = homeLogo, name = homeName)
        Text("VS", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        TeamColumn(logo = awayLogo, name = awayName)
    }
}

@Composable
fun TeamColumn(logo: String?, name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = logo,
            contentDescription = name,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(40.dp))
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

@Composable
fun StatRow(
    homeStat: FixtureStatisticItem,
    awayStat: FixtureStatisticItem
) {
    val homeValue = parseStat(homeStat.value)
    val awayValue = parseStat(awayStat.value)
    val total = (homeValue + awayValue).takeIf { it > 0 } ?: 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    ) {
        Text(
            text = homeStat.type,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = homeStat.value?.toString() ?: "-",
                fontSize = 13.sp,
                modifier = Modifier.width(40.dp)
            )

            StatBar(
                homePercent = homeValue.toFloat() / total,
                awayPercent = awayValue.toFloat() / total
            )

            Text(
                text = awayStat.value?.toString() ?: "-",
                fontSize = 13.sp,
                modifier = Modifier.width(40.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )
        }
    }
}

@Composable
fun StatBar(homePercent: Float, awayPercent: Float) {

    // evitar crash: nunca permitir 0f
    val safeHome = if (homePercent <= 0f) 0.0001f else homePercent
    val safeAway = if (awayPercent <= 0f) 0.0001f else awayPercent

    Box(
        modifier = Modifier
            //.weight(1f)
            .height(18.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.15f))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(safeHome)
                    .background(Color(0xFF4CAF50))
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(safeAway)
                    .background(Color(0xFFD32F2F))
            )
        }
    }
}

fun parseStat(value: Any?): Int {
    return when (value) {
        is Int -> value
        is String -> value.replace("%", "").trim().toIntOrNull() ?: 0
        else -> 0
    }
}
