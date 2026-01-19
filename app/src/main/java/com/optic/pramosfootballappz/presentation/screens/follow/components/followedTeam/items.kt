package com.optic.pramosfootballappz.presentation.screens.follow.components.followedTeam


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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.optic.pramosfootballappz.domain.model.Team
import com.optic.pramosfootballappz.presentation.components.follow.FollowButton
import com.optic.pramosfootballappz.presentation.components.follow.UnFollowButtonAlternative
import com.optic.pramosfootballappz.presentation.navigation.Graph

@Composable
fun FollowedTeamItem(
    team: Team,
    backgroundColor: Color,
    onUnFollowClick: (Int) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(vertical = 1.dp, horizontal = 1.dp)
            .clickable { team.id?.let { navController.navigate("${Graph.TEAM}/$it") } },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor // ahora usa color como jugadores seguidos
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

                    // Logo principal del equipo
                    AsyncImage(
                        model = team.logo ?: "",
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = team.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.White
                )
            }

            // Botón UNFOLLOW igual que jugadores
            UnFollowButtonAlternative(
                onClick = { team.id?.let { onUnFollowClick(it) } }
            )
        }
    }
}

@Composable
fun SuggestedTeamItem(
    team: Team,
    navController: NavHostController,
    onFollowClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(vertical = 1.dp, horizontal = 1.dp)
            .clickable {
                team.id?.let { navController.navigate("${Graph.TEAM}/$it") }
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = team.logo ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = team.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            FollowButton(
                onClick = { team.id?.let { onFollowClick(it) } }
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

