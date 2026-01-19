package com.optic.pramosfootballappz.presentation.screens.prode.ranking


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.domain.model.prode.*
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.presentation.components.BackTopBar
import com.optic.pramosfootballappz.presentation.screens.prode.ProdeViewModel

@Composable
fun UserPredictionSummaryScreen(
    userId: Int,
    navController: NavHostController,
    vm: ProdeViewModel,
    modifier: Modifier = Modifier
) {
    val summaryState by vm.userPredictionSummary.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(userId) {
        vm.getUserPredictionSummary(userId, 2025)
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = "Prediction Summary",
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {

            when (val result = summaryState) {

                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Success -> {
                    val summary = result.data ?: return@Column

                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {

                        item {
                            UserOverviewCard(summary)
                        }

                        items(summary.leagues) { league ->
                            LeagueSummaryItemModern(league)
                        }

                        summary.lastPrediction?.let { last ->
                            item {
                                LastPredictionCard(last)
                            }
                        }
                    }
                }

                is Resource.Failure -> {
                    Text(
                        text = "Error loading summary",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {}
            }
        }
    }
}

/* -------------------- OVERVIEW -------------------- */

@Composable
fun UserOverviewCard(summary: UserPredictionSummaryEnriched) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Season ${summary.season}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${summary.totalPoints}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "pts",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatChip("Fixtures", summary.totalFixtures.toString())
                StatChip("Ranking", summary.rankingPosition?.toString() ?: "-")
            }
        }
    }
}

@Composable
fun StatChip(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/* -------------------- LEAGUE -------------------- */

@Composable
fun LeagueSummaryItemModern(
    leagueSummary: LeaguePredictionSummaryEnriched
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = leagueSummary.league.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "${leagueSummary.totalPoints} pts",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${leagueSummary.cantFixtures} fixtures played",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            leagueSummary.fixture?.let {
                Spacer(modifier = Modifier.height(10.dp))
                FixturePredictionMiniRow(it)
            }
        }
    }
}

/* -------------------- FIXTURE -------------------- */

@Composable
fun FixturePredictionMiniRow(
    enriched: FixturePredictionEnriched
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(10.dp)) {

            Text(
                text = enriched.fixture.leagueRound ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${enriched.fixture.teamHome.name} vs ${enriched.fixture.teamAway.name}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PredictionChip("Pick", enriched.prediction.prediction)
                PredictionChip(
                    "Score",
                    "${enriched.prediction.goalsHome ?: "-"} - ${enriched.prediction.goalsAway ?: "-"}"
                )
                enriched.prediction.isCorrect?.let {
                    PredictionChip("Result", if (it) "✔" else "✘")
                }
            }
        }
    }
}

@Composable
fun PredictionChip(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/* -------------------- LAST PREDICTION -------------------- */

@Composable
fun LastPredictionCard(last: LastPredictionSummaryEnriched) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Text(
                text = "Last Prediction",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${last.fixture.teamHome.name} vs ${last.fixture.teamAway.name}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = last.createdAt,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}


