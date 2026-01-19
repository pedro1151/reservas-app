package com.optic.pramosfootballappz.presentation.screens.fixtures.detail.components.lineups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.optic.pramosfootballappz.domain.model.fixture.lineups.FixtureLineupItem
import com.optic.pramosfootballappz.domain.model.fixture.lineups.FixturePlayerItem
import com.optic.pramosfootballappz.domain.util.Resource
import com.optic.pramosfootballappz.presentation.screens.fixtures.detail.FixtureDetailViewModel

@Composable
fun FixtureLineupsScreen(
    paddingValues: PaddingValues,
    fixtureId: Int
) {
    val viewModel: FixtureDetailViewModel = hiltViewModel()
    val lineupsState by viewModel.lineupsFixtureState.collectAsState()

    LaunchedEffect(fixtureId) {
        viewModel.getFixtureLineups(fixtureId)
    }

    when (lineupsState) {
        is Resource.Loading -> Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        is Resource.Success -> {
            val lineups = (lineupsState as Resource.Success).data?.response ?: emptyList()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(lineups) { lineup ->
                    FixtureFormationCard(lineup)
                }

                if (lineups.size == 2) {
                    item {
                        FixtureSubstitutesCard(
                            homeLineup = lineups[0],
                            awayLineup = lineups[1]
                        )
                    }
                }
            }
        }

        is Resource.Failure -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (lineupsState as Resource.Failure).message ?: "Error al cargar alineaciones",
                color = MaterialTheme.colorScheme.error
            )
        }

        else -> {}
    }
}

@Composable
fun FixtureFormationCard(lineup: FixtureLineupItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🏳️ Logo y formación
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = lineup.team.logo,
                    contentDescription = lineup.team.name,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = lineup.team.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Text(
                text = "Formación: ${lineup.formation ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ⚽ Campo con jugadores
            FootballField(
                players = lineup.startXI,
                formation = lineup.formation ?: ""
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 👨‍🏫 Entrenador
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = lineup.coach.photo,
                    contentDescription = lineup.coach.name,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Entrenador: ${lineup.coach.name}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FixtureSubstitutesCard(
    homeLineup: FixtureLineupItem,
    awayLineup: FixtureLineupItem
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🏷️ Título centrado
            Text(
                text = "Suplentes",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 👨‍🏫 Contenido (dos columnas)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                // 🏠 Local
                TeamSubstitutesColumn(lineup = homeLineup)

                // ✈️ Visitante
                TeamSubstitutesColumn(lineup = awayLineup)
            }
        }
    }
}

@Composable
fun TeamSubstitutesColumn(lineup: FixtureLineupItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(150.dp)
    ) {
        AsyncImage(
            model = lineup.team.logo,
            contentDescription = lineup.team.name,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            lineup.substitutes.forEach { playerItem ->
                SubstitutePlayerItem(playerItem)
            }
        }
    }
}

@Composable
fun SubstitutePlayerItem(playerItem: FixturePlayerItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        AsyncImage(
            model = playerItem.player.photo,
            contentDescription = playerItem.player.name,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = playerItem.player.number?.toString() ?: "-",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = playerItem.player.name.split(" ").lastOrNull() ?: playerItem.player.name,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

@Composable
fun FootballField(players: List<FixturePlayerItem>, formation: String) {
    val gridMap = remember(players) {
        players.mapNotNull { playerItem ->
            val grid = playerItem.player.grid ?: return@mapNotNull null
            val (row, col) = grid.split(":").mapNotNull { it.toIntOrNull() }
            Triple(row, col, playerItem)
        }.groupBy { it.first } // Agrupar por fila (1 = arquero, etc.)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .background(Color(0xFF1B5E20), shape = RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            gridMap.toSortedMap().forEach { (_, playersInRow) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    playersInRow.sortedBy { it.second }.forEach { (_, _, playerItem) ->
                        PlayerOnField(playerItem)
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerOnField(playerItem: FixturePlayerItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.width(70.dp)
    ) {
        AsyncImage(
            model = playerItem.player.photo,
            contentDescription = playerItem.player.name,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f)),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = playerItem.player.number?.toString() ?: "-",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = playerItem.player.name.split(" ").lastOrNull() ?: playerItem.player.name,
                fontSize = 11.sp,
                color = Color.White,
                maxLines = 1
            )
        }
    }
}
