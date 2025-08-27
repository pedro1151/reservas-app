package com.optic.ecommerceappmvvm.presentation.screens.leagues.league.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.team.TeamResponse

@Composable
fun LeagueHeader(league: League, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = league.logo,
                contentDescription = "Foto del EQuipo",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    color = Color.White,
                    text = "${league.name}",
                    style = MaterialTheme.typography.headlineSmall // equivalente a h6 en M3
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    /*
                    AsyncImage(
                        model = "https://media.api-sports.io/football/teams/548.png",
                        contentDescription = "Equipo",
                        modifier = Modifier.size(24.dp)
                    )

                     */
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${league.country?.name}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium // equivalente body2
                    )
                }
            }
        }
    }
}