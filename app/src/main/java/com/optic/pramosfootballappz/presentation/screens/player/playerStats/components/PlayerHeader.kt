package com.optic.pramosfootballappz.presentation.screens.player.playerStats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.optic.pramosfootballappz.domain.model.player.PlayerComplete
import com.optic.pramosfootballappz.domain.util.Resource

@Composable
fun PlayerHeader(
    player: Resource<PlayerComplete>
) {

    when (player) {

        is Resource.Loading -> {
            // 🟡 Estado loading
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        is Resource.Failure -> {
            // 🔴 Estado error
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp, vertical = 8.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = player.message ?: "Error al cargar información del jugador",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        is Resource.Success -> {
            // 🟢 Estado success
            val data = player.data ?: return
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = data.photo,
                        contentDescription = "Foto del Jugador",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "${data.firstname} ${data.lastname}",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall // equivalente a h6 en M3
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {

                                AsyncImage(
                                    model = data.lastTeam?.logo,
                                    contentDescription = "Equipo",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = data.lastTeam?.name ?:"-",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium // equivalente body2
                                )


                        }
                    }
                }
            }
        }

        else -> {}
    }
}


