package com.optic.ecommerceappmvvm.presentation.screens.follow.components.followedPlayer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.optic.ecommerceappmvvm.domain.model.player.Player
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.optic.ecommerceappmvvm.presentation.components.follow.FollowButton
import com.optic.ecommerceappmvvm.presentation.components.follow.UnFollowButton
import com.optic.ecommerceappmvvm.presentation.components.follow.UnFollowButtonAlternative
import com.optic.ecommerceappmvvm.presentation.navigation.Graph
import com.optic.ecommerceappmvvm.presentation.ui.theme.getGreenColorFixture
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import com.optic.ecommerceappmvvm.presentation.screens.follow.FollowViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FinalPlayerList(
    players: List<Player>,
    followedPlayers: List<Player>,
    navController: NavHostController,
    onFollowClick: (Int) -> Unit = {},
    onUnFollowClick: (Int) -> Unit = {},
    paddingValues: PaddingValues,
    viewModel: FollowViewModel
) {

    val colors = listOf(
        Color(0xFFBB86FC), Color(0xFF03DAC5), Color(0xFFFFB74D),
        Color(0xFF4CAF50), Color(0xFFE91E63), Color(0xFF2196F3),
        Color(0xFFFF5722), Color(0xFF9C27B0), Color(0xFF00BCD4),
        Color(0xFFFFC107), Color(0xFF8BC34A), Color(0xFFE040FB)
    )

    fun colorForIndex(index: Int) = colors[index % colors.size]

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 1.dp, horizontal = 1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {

        // ---------------------------------------------------------
        // 🔥 1. SECCIÓN: PLAYERS SEGUIDOS CON ANIMACIONES
        // ---------------------------------------------------------

        items(followedPlayers.chunked(2)) { row ->

            AnimatedVisibility(
                visible = row.isNotEmpty(),
                modifier = Modifier.animateItemPlacement()
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    row.forEachIndexed { index, player ->

                        AnimatedVisibility(
                            visible = true,
                            modifier = Modifier
                                .weight(1f)
                                .animateItemPlacement()
                        ) {
                            FollowedPlayerItem(
                                player = player,
                                backgroundColor = colorForIndex(index),
                                onUnFollowClick = onUnFollowClick,
                                navController = navController,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    if (row.size < 2) {
                        Spacer(
                            Modifier
                                .weight(1f)
                                .animateItemPlacement()
                        )
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // 📌 SEPARADOR MODERNO ENTRE LISTAS
        // ---------------------------------------------------------

        item {
            AnimatedVisibility(visible = true, modifier = Modifier.animateItemPlacement()) {
                SectionDividerLabel(text = "Jugadores sugeridos")
            }
        }

        // ---------------------------------------------------------
        // 🔥 2. SECCIÓN: JUGADORES SUGERIDOS CON ANIMACIÓN
        // ---------------------------------------------------------

        items(players, key = { it.id ?: 0 }) { player ->

            AnimatedVisibility(
                visible = true,
                modifier = Modifier.animateItemPlacement()
            ) {
                SuggestedPlayerItem(
                    player = player,
                    navController = navController,
                    onFollowClick = onFollowClick
                )
            }
        }
    }

    // --- Scroll listener para paginación ---
    LaunchedEffect(players) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                // cuando el usuario scrollea cerca del final
                if (lastVisibleItem >= totalItems - 5) {
                    coroutineScope.launch {
                        coroutineScope.launch {
                            viewModel.getPlayers("null")
                        } // carga la siguiente página }
                    }
                }
            }
    }
}



