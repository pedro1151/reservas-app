package com.optic.pramosfootballappz.presentation.screens.player.players.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.optic.pramosfootballappz.domain.model.player.Player
import com.optic.pramosfootballappz.presentation.components.follow.FollowButton
import com.optic.pramosfootballappz.presentation.navigation.Graph

@Composable
fun PlayerListContent(
    modifier: Modifier = Modifier,
    players: List<Player>,
    paddingValues: PaddingValues,
    navController: NavHostController,
    onFollowClick: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(PaddingValues(1.dp))
            .fillMaxSize()

            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        players.forEach { player ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 1.dp)
                    .clickable {
                        player.id?.let {
                            navController.navigate("${Graph.PLAYER}/$it")
                        }
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(50.dp)) {
                            AsyncImage(
                                model = player.photo ?: "",
                                contentDescription = "Foto de ${player.firstname} ${player.lastname}",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(15.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            ) {
                                AsyncImage(
                                    model =  player.lastTeam?.logo?: "",
                                    contentDescription = "Logo del equipo",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "${player.firstname} ${player.lastname}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    FollowButton(
                        onClick = {
                            player.id?.let { onFollowClick(it) }
                        }
                    )
                }
            }
        }
    }
}
