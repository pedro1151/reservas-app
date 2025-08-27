package com.optic.ecommerceappmvvm.presentation.screens.client.playerStats.statistics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerWithStats
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerStats
import com.optic.ecommerceappmvvm.presentation.screens.client.playerStats.components.InfoText

@ExperimentalFoundationApi
@Composable
fun PlayerStatsContentTab(
    paddingValues: PaddingValues,
    playerStats: PlayerWithStats
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(playerStats.statistics) { stat ->
            PlayerStatisticCard(stat)
        }
    }
}


@Composable
fun PlayerStatisticCard(stat: PlayerStats) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = stat.league.logo,
                    contentDescription = "League Logo",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stat.league.name,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = stat.team.logo,
                            contentDescription = "Team Logo",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Equipo: ${stat.team.name}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Divider()

                    Spacer(modifier = Modifier.height(8.dp))

                    InfoText("Temporada", stat.season.toString())
                    InfoText("Posición", stat.gamesPosition.toString())
                    InfoText("Apariciones", stat.gamesAppearences.toString())
                    InfoText("Minutos", stat.gamesMinutes.toString())
                    InfoText("Goles", stat.goalsTotal.toString())
                    InfoText("Asistencias", stat.goalsTotalAssists.toString())
                    InfoText("Pases precisos", stat.passesAccuracy.toString())
                    InfoText("Faltas cometidas", stat.foulsCommitted.toString())
                    InfoText("Tarjetas amarillas", stat.cardsYellow.toString())
                    InfoText("Penales anotados", stat.penaltyScored.toString())
                }
            }
        }
    }
}