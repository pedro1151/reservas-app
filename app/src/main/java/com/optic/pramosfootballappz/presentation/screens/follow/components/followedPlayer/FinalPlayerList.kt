package com.optic.pramosfootballappz.presentation.screens.follow.components.followedPlayer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.optic.pramosfootballappz.domain.model.player.Player
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.items
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.input.pointer.pointerInput
import com.optic.pramosfootballappz.presentation.screens.follow.FollowViewModel
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext
import com.optic.pramosfootballappz.R

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

    // para idioma
    val localizedContext = LocalizedContext.current


    val colors = listOf(
        Color(0xFFBB86FC), Color(0xFF03DAC5), Color(0xFFFFB74D),
        Color(0xFF4CAF50), Color(0xFFE91E63), Color(0xFF2196F3),
        Color(0xFFFF5722), Color(0xFF9C27B0), Color(0xFF00BCD4),
        Color(0xFFFFC107), Color(0xFF8BC34A), Color(0xFFE040FB)
    )

    fun colorForIndex(index: Int) = colors[index % colors.size]

   // val listState = rememberLazyListState()
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 1.dp, horizontal = 1.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    // Solo bloqueamos horizontal
                    if (dragAmount.x != 0f) {
                        change.consume()
                    }
                }
            }
,
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
                SectionDividerLabel(
                    text =  localizedContext.getString(R.string.siguiendo_screen_jugadores_sugeridos_title)
                )
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

                if (viewModel.isLoadingPage) {
                    this@LazyColumn.item{
                        CircularProgressIndicator()
                    }
                }

            }
        }

    }

    // --- Scroll listener para paginación ---
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                val total = listState.layoutInfo.totalItemsCount
                if (index >= total - 10) {
                    viewModel.getPlayers("null")
                }
            }
    }

}


