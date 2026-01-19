package com.optic.pramosfootballappz.presentation.screens.player.playerStats.trayectoria

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.optic.pramosfootballappz.domain.model.Team

import com.optic.pramosfootballappz.domain.model.player.playerteams.PlayerTeamsResponse
import com.optic.pramosfootballappz.domain.util.Resource

@Composable
fun PlayerTrayectoryTab(
    paddingValues: PaddingValues,
    state: Resource<PlayerTeamsResponse>
) = when (state) {
    is Resource.Loading -> {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(strokeWidth = 2.dp)
        }
    }

    is Resource.Failure -> {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error al cargar la trayectoria",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    is Resource.Success -> {
        val response = state.data
        if (response == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontró información del jugador",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 1.dp, vertical = 8.dp)
                    .fillMaxSize() // ← Esto es CLAVE para que use todo el alto
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(6.dp)
              //  contentPadding = PaddingValues(horizontal = 5.dp, vertical = 8.dp)
            ) {

                // Equipos por season
                response.teams.forEach { teamWithSeasons ->
                    // Ordenar seasons descendente
                    val sortedSeasons = teamWithSeasons.seasons.sortedDescending()
                    items(sortedSeasons) { season ->
                        TeamTrajectoryCard(teamWithSeasons.team, season)
                    }
                }
            }
        }
    }

    else -> {}
}


@Composable
fun TeamTrajectoryCard(team: Team, season: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = team.logo,
                contentDescription = team.name,
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${season} / ${season+1}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}