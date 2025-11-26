package com.optic.ecommerceappmvvm.presentation.screens.games.list

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
import com.optic.ecommerceappmvvm.domain.model.trivias.game.GameResponse
import com.optic.ecommerceappmvvm.domain.util.Resource
import kotlinx.coroutines.flow.StateFlow
@Composable
fun GameList(
    modifier: Modifier = Modifier,
    games: Resource<List<GameResponse>>,
    onUnFollowClick: (Int) -> Unit = {},
    navController: NavHostController
) {
    val colors = listOf(
        Color(0xFFBB86FC),
        Color(0xFF03DAC5),
        //Color(0xFFFFB74D),
        //Color(0xFF4CAF50),
        //Color(0xFFE91E63),
        //Color(0xFF2196F3),
        //Color(0xFFFF5722),
        // Color(0xFF9C27B0),
        // Color(0xFF00BCD4),
        // Color(0xFFFFC107),
        //Color(0xFF8BC34A),
        Color(0xFFE040FB)
    )

    fun colorForIndex(index: Int) = colors[index % colors.size]

    val listState = rememberLazyListState()

    when (games) {
        is Resource.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando juegos...", color = Color.Gray)
            }
        }

        is Resource.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize()
               // contentAlignment = Alignment.Center
            ) {
                Text("Error: ${games.message}", color = Color.Red)
            }
        }

        is Resource.Success -> {
            val gameList = games.data ?: emptyList()

            LazyColumn(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 12.dp),
                state = listState
            ) {
                items(gameList.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val rowIndex = gameList.chunked(2).indexOf(rowItems)
                        rowItems.forEachIndexed { index, game ->
                            val globalIndex = rowIndex * 2 + index
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        navController.navigate("${Graph.GAME_DIFICULTY}/${game.code}")
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
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Top,
                                        horizontalAlignment = Alignment.CenterHorizontally // 🔹 eje X
                                    ) {
                                        // 🔹 Logo en la parte superior izquierda
                                        AsyncImage(
                                            model = game.logo, // url del logo
                                            contentDescription = "Logo del juego",
                                            modifier = Modifier
                                                .size(70.dp)
                                                .clip(CircleShape)
                                                .background(Color.White.copy(alpha = 0.2f))
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // 🔹 Nombre del juego
                                        Text(
                                            text = game.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White
                                        )

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

            LaunchedEffect(gameList.size) {
                if (gameList.isNotEmpty()) {
                    delay(300L)
                    val lastRowIndex = (gameList.size - 1) / 2
                    listState.animateScrollToItem(lastRowIndex)
                }
            }
        }

        else -> {}
    }
}
