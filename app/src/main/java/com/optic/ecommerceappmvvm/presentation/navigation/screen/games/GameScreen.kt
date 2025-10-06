package com.optic.ecommerceappmvvm.presentation.navigation.screen.games

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class GameScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object Games: GameScreen(
        route = "client/games",
        title = "Games",
        icon = Icons.Default.PlayArrow
    )

    object Dificulty: GameScreen(
        route = "client/games/dificulty",
        title = "Dificulty",
        icon = Icons.Default.PlayArrow
    )



}
