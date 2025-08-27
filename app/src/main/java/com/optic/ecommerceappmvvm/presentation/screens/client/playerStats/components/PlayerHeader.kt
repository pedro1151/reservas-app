package com.optic.ecommerceappmvvm.presentation.screens.client.playerStats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.domain.model.player.playerteams.PlayerLastTeamResponse
import com.optic.ecommerceappmvvm.domain.model.player.stats.PlayerWithStats
import com.optic.ecommerceappmvvm.domain.util.Resource

@Composable
fun PlayerHeader(
    player: PlayerWithStats,
    paddingValues: PaddingValues,
    playerLastTeamState : Resource<PlayerLastTeamResponse>
) {



    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = player.photo,
                contentDescription = "Foto del Jugador",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${player.firstname} ${player.lastname}",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall // equivalente a h6 en M3
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Recupero el equipo solo si es sucess
                    if (playerLastTeamState is Resource.Success) {
                        val lastTeamData = (playerLastTeamState as Resource.Success).data
                        AsyncImage(
                            model = lastTeamData.lastTeam.logo,
                            contentDescription = "Equipo",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = lastTeamData.lastTeam.name,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium // equivalente body2
                        )
                    }

                }
            }
        }
    }
}