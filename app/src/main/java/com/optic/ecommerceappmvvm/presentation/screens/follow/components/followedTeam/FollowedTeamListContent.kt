package com.optic.ecommerceappmvvm.presentation.screens.follow.components.followedTeam


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.player.Player
import com.optic.ecommerceappmvvm.presentation.components.follow.UnFollowButton
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import kotlinx.coroutines.delay
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.Team

@Composable
fun FollowedTeamListContent(
    modifier: Modifier = Modifier,
    followedTeams: List<Team>,
    onUnFollowClick: (Int) -> Unit = {},
    navController: NavHostController
) {
    val colors = listOf(
        Color(0xFFBB86FC), // Purple 200
        Color(0xFF03DAC5), // Teal 200
        Color(0xFFFFB74D), // Orange 300
        Color(0xFF4CAF50), // Green 500
        Color(0xFFE91E63), // Pink 500
        Color(0xFF2196F3), // Blue 500
        Color(0xFFFF5722), // Deep Orange 500
        Color(0xFF9C27B0), // Purple 500
        Color(0xFF00BCD4), // Cyan 500
        Color(0xFFFFC107), // Amber 500
        Color(0xFF8BC34A), // Light Green 500
        Color(0xFFE040FB)  // Purple Accent
    )

    fun colorForIndex(index: Int) = colors[index % colors.size]

    Column(
        modifier = modifier
            .padding(1.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        val rows = followedTeams.chunked(2)

        rows.forEachIndexed { rowIndex, rowItems ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                rowItems.forEachIndexed { index, player ->
                    val globalIndex = rowIndex * 2 + index

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                player.id?.let {
                                    navController.navigate("${Graph.TEAM}/$it")
                                }
                            },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(colorForIndex(globalIndex))
                                .fillMaxSize()
                                .padding(2.dp)
                        ) {

                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    AsyncImage(
                                        model = player.logo ?: "",
                                        contentDescription = "Logo de ${player.name}",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                    )

                                    UnFollowButton(
                                        onClick = {
                                            player.id?.let { onUnFollowClick(it) }
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = player.name ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // Si la fila tiene solo 1 ítem, completamos el espacio.
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

/*
@Composable
fun FollowedTeamListContent(
    modifier: Modifier = Modifier,
    followedTeams: List<Team>,
    onUnFollowClick: (Int) -> Unit = {},
    navController: NavHostController
) {
    val colors = listOf(
        Color(0xFFBB86FC), // Purple 200
        Color(0xFF03DAC5), // Teal 200
        Color(0xFFFFB74D), // Orange 300
        Color(0xFF4CAF50), // Green 500
        Color(0xFFE91E63), // Pink 500
        Color(0xFF2196F3), // Blue 500
        Color(0xFFFF5722), // Deep Orange 500
        Color(0xFF9C27B0), // Purple 500
        Color(0xFF00BCD4), // Cyan 500
        Color(0xFFFFC107), // Amber 500
        Color(0xFF8BC34A), // Light Green 500
        Color(0xFFE040FB)  // Purple Accent
    )

    fun colorForIndex(index: Int) = colors[index % colors.size]

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 12.dp),
        state = listState
    ) {
        items(followedTeams.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val rowIndex = followedTeams.chunked(2).indexOf(rowItems)
                rowItems.forEachIndexed { index, player ->
                    val globalIndex = rowIndex * 2 + index
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                player.id?.let {
                                    navController.navigate("${Graph.TEAM}/$it")
                                }
                            },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(colorForIndex(globalIndex))
                                .fillMaxSize()
                                .padding(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    AsyncImage(
                                        model = player.logo ?: "",
                                        contentDescription = "Foto de ${player.name}",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                    )

                                    UnFollowButton(
                                        onClick = {
                                            player.id?.let { onUnFollowClick(it) }
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = player.name ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    LaunchedEffect(followedTeams.size) {
        if (followedTeams.isNotEmpty()) {
            delay(300L)
            val lastRowIndex = (followedTeams.size - 1) / 2
            listState.animateScrollToItem(lastRowIndex)
        }
    }
}

 */