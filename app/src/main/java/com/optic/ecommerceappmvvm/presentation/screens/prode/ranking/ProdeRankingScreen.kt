package com.optic.ecommerceappmvvm.presentation.screens.prode.ranking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.prode.UserPredictionRanking
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.components.BackTopBar
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.prode.ProdeViewModel

@Composable
fun ProdeRankingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    vm: ProdeViewModel
) {
    var expanded by remember { mutableStateOf(true) }
    val isSaving by vm.isSaving.collectAsState()

    val ranking by vm.ranking.collectAsState()
    LaunchedEffect(Unit) {
        vm.getPredictionRanking(10)
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = "Ranking",
                navController = navController,
            )
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 1.dp, vertical = 1.dp)
                .padding(paddingValues)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {}

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                when (val result = ranking) {

                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is Resource.Success -> {

                        val ranklist = result.data ?: emptyList()
                        val listState = rememberLazyListState()

                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            items(ranklist) { rankitem ->
                                RankingItem(
                                    rankitem = rankitem,
                                    navController = navController

                                )
                            }
                        }
                    }

                    is Resource.Failure -> {
                        Text(
                            text = "Error al cargar el ranking",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun RankingItem(
    rankitem: UserPredictionRanking,
    onFollowClick: () -> Unit = {},
    navController: NavHostController
) {

    val medalColor = when (rankitem.ranking) {
        1 -> Color(0xFFFFD700) // 🥇 Oro
        2 -> Color(0xFFC0C0C0) // 🥈 Plata
        3 -> Color(0xFFCD7F32) // 🥉 Bronce
        else -> MaterialTheme.colorScheme.surface
    }

    val medalTextColor = if (rankitem.ranking <= 3)
        Color.Black
    else
        MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable {
                navController.navigate("${Graph.USER_PREDICTION}/${rankitem.userId}")

            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🏅 MEDALLA / RANK
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        medalColor,
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rankitem.ranking.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = medalTextColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 👤 USER INFO
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = rankitem.username,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = rankitem.email,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = rankitem.userId.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                )
            }

            // ⭐ PUNTOS
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${rankitem.pointsAwarded}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "pts",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
