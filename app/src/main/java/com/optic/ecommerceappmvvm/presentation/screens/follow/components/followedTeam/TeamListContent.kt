package com.optic.ecommerceappmvvm.presentation.screens.follow.components.followedTeam

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.presentation.components.follow.FollowButton
import com.optic.ecommerceappmvvm.presentation.navigation.Graph


@Composable
fun TeamListContent(
    modifier: Modifier = Modifier,
    teams: List<Team>,
    paddingValues: PaddingValues,
    navController: NavHostController,
    onFollowClick: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        teams.forEach { team ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(horizontal = 1.dp, vertical = 1.dp)
                    .clickable {
                        team.id?.let {
                            navController.navigate("${Graph.TEAM}/$it")
                        }
                    },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = team.logo ?: "",
                        contentDescription = "Logo de ${team.name}",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = team.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    FollowButton(
                        onClick = {
                            team.id?.let { onFollowClick(it) }
                        }
                    )
                }
            }
        }
    }
}
