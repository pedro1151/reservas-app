package com.optic.pramosfootballappz.presentation.screens.follow.components.followedPlayer


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.optic.pramosfootballappz.domain.model.player.Player
import androidx.navigation.NavHostController
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.optic.pramosfootballappz.presentation.components.follow.FollowButton
import com.optic.pramosfootballappz.presentation.components.follow.UnFollowButtonAlternative
import com.optic.pramosfootballappz.presentation.navigation.Graph

@Composable
fun SuggestedPlayerItem(
    player: Player,
    navController: NavHostController,
    onFollowClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(vertical = 1.dp, horizontal = 1.dp)
            .clickable { player.id?.let { navController.navigate("${Graph.PLAYER}/$it") } },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(50.dp)) {
                    AsyncImage(
                        model = player.photo ?: "",
                        contentDescription = null,
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
                            model = player.lastTeam?.logo ?: "",
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "${player.firstname} ${player.lastname}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            FollowButton(
                onClick = { player.id?.let { onFollowClick(it) } }
            )
        }
    }
}

@Composable
fun FollowedPlayerItem(
    player: Player,
    backgroundColor: Color,
    onUnFollowClick: (Int) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(75.dp)
            .padding(vertical = 1.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { player.id?.let { navController.navigate("${Graph.PLAYER}/$it") } },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor //Color(0xFF1E1E1E) // Grafito moderno
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // FOTO + NOMBRE
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = player.photo ?: "",
                    contentDescription = "Foto",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = player.firstname ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            // UNFOLLOW BUTTON
            UnFollowButtonAlternative(
                onClick = { player.id?.let { onUnFollowClick(it) } }
            )
        }
    }
}

@Composable
fun SectionDividerLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Línea izquierda
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        // Texto centrado
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        // Línea derecha
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
    }
}
