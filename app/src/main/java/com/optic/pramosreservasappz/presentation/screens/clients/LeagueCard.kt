package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.optic.pramosreservasappz.presentation.components.follow.FollowButton
import com.optic.pramosreservasappz.presentation.components.follow.UnFollowButton
import com.optic.pramosreservasappz.presentation.navigation.Graph
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse


// CARD PARA MOSTRAR LAS LIGAS
@Composable
fun LeagueCard(
    league: ClientResponse,
    isFollowed: Boolean,
    onFollowClick: () -> Unit,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(horizontal = 1.dp, vertical = 0.dp)
            .clickable {
                league.id?.let {
                    navController.navigate("${Graph.LEAGUE}/$it")
                }

            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {


                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = league.fullName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // ✅ Acá es correcto usar un Composable
            if (isFollowed){
                UnFollowButton(
                    onClick = onFollowClick
                )

            }
            else{
                FollowButton(
                    onClick = onFollowClick
                )
            }

        }
    }
}