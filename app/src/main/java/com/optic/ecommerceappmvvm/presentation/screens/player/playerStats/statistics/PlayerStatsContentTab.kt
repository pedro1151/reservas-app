package com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerStats
import com.optic.ecommerceappmvvm.presentation.screens.player.playerStats.components.InfoText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.* // todos los íconos extendidos

@Composable
fun PlayerStatsContentTab(
    paddingValues: PaddingValues,
    playerStats: PlayerWithStats
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 1.dp, vertical = 3.dp)
            .fillMaxSize()
       // .padding(horizontal = 8.dp, vertical = 8.dp),
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
            .fillMaxSize()
            .clickable { expanded = !expanded }
            .padding(horizontal = 1.dp, vertical = 3.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabecera: Liga
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = stat.league.logo,
                    contentDescription = "League Logo",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stat.league.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {

                    // Equipo
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = stat.team.logo,
                            contentDescription = "Team Logo",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stat.team.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))

                    // --- Sección: Juegos ---
                    SectionHeader("Juegos", Icons.Outlined.SportsSoccer)
                    InfoText("Posición", stat.gamesPosition ?: "-")
                    InfoText("Apariciones", (stat.gamesAppearences ?: 0).toString())
                    InfoText("Titularidades", (stat.gamesLineups ?: 0).toString())
                    InfoText("Minutos", (stat.gamesMinutes ?: 0).toString())
                    InfoText("Número de camiseta", (stat.gamesNumber ?: 0).toString())
                    InfoText("Rating", stat.gamesRating?.let { String.format("%.1f", it) } ?: "0.0")
                    InfoText("Capitán", if (stat.gamesCaptain == true) "Sí" else "No")

                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader("Goles", Icons.Outlined.SportsSoccer)
                    InfoText("Goles totales", (stat.goalsTotal ?: 0).toString())
                    InfoText("Asistencias", (stat.goalsTotalAssists ?: 0).toString())
                    InfoText("Goles concedidos", (stat.goalsConceded ?: 0).toString())
                    InfoText("Atajadas", (stat.goalsSaves ?: 0).toString())

                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader("Pases", Icons.Outlined.SyncAlt)
                    InfoText("Total", (stat.passesTotal ?: 0).toString())
                    InfoText("Clave", (stat.passesKey ?: 0).toString())
                    InfoText("Precisión (%)", (stat.passesAccuracy ?: 0).toString())

                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader("Defensiva", Icons.Outlined.Security)
                    InfoText("Entradas", (stat.tacklesTotal ?: 0).toString())
                    InfoText("Bloqueos", (stat.tacklesBlocks ?: 0).toString())
                    InfoText("Intercepciones", (stat.tacklesInterceptions ?: 0).toString())

                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader("Duelos & Dribbles", Icons.Outlined.Groups)
                    InfoText("Duelos totales", (stat.duelsTotal ?: 0).toString())
                    InfoText("Duelos ganados", (stat.duelsWon ?: 0).toString())
                    InfoText("Regates intentados", (stat.dribblesAttempts ?: 0).toString())
                    InfoText("Regates exitosos", (stat.dribblesSuccess ?: 0).toString())
                    InfoText("Regates recibidos", (stat.dribblesPast ?: 0).toString())

                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader("Faltas", Icons.Outlined.WarningAmber)
                    InfoText("Recibidas", (stat.foulsDrawn ?: 0).toString())
                    InfoText("Cometidas", (stat.foulsCommitted ?: 0).toString())

                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader("Disparos", Icons.Outlined.SportsHandball)
                    InfoText("Total", (stat.shotsTotal ?: 0).toString())
                    InfoText("A puerta", (stat.shotsOn ?: 0).toString())

                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader("Sustituciones", Icons.Outlined.SwapHoriz)
                    InfoText("Ingresos", (stat.substitutesIn ?: 0).toString())
                    InfoText("Salidas", (stat.substitutesOut ?: 0).toString())
                    InfoText("Banco", (stat.substitutesBench ?: 0).toString())

                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader("Tarjetas", Icons.Outlined.Flag)
                    InfoText("Amarillas", (stat.cardsYellow ?: 0).toString())
                    InfoText("Doble amarilla", (stat.cardsYellowRed ?: 0).toString())
                    InfoText("Rojas", (stat.cardsRed ?: 0).toString())

                    Spacer(modifier = Modifier.height(12.dp))
                    SectionHeader("Penales", Icons.Outlined.Sports)
                    InfoText("Ganados", if (stat.penaltyWon == true) "Sí" else "No")
                    InfoText("Cometidos", if (stat.penaltyCommited == true) "Sí" else "No")
                    InfoText("Anotados", (stat.penaltyScored ?: 0).toString())
                    InfoText("Fallados", (stat.penaltyMissed ?: 0).toString())
                    InfoText("Atajados", (stat.penaltySaved ?: 0).toString())
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
