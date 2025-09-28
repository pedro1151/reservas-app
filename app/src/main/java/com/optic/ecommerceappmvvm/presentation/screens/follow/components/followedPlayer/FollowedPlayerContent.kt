package com.optic.ecommerceappmvvm.presentation.screens.follow.components.followedPlayer


import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.optic.ecommerceappmvvm.domain.model.player.Player
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.presentation.screens.player.players.list.components.PlayerListContent


@Composable
fun FollowedPlayerContent(
    players: List<Player>,
    followedPlayers: List<Player>,
    navController: NavHostController,
    onFollowClick: (Int) -> Unit = {},
    onUnFollowClick: (Int) -> Unit = {},
    paddingValues: PaddingValues
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {

        // Parte superior: jugadores seguidos
        FollowedPlayerListContent(
            modifier = Modifier,
            followedPlayers = followedPlayers,
            onUnFollowClick = onUnFollowClick,
            navController = navController
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Parte inferior: lista completa de jugadores
        PlayerListContent(
            modifier = Modifier.fillMaxSize(),
            players = players,
            navController = navController,
            paddingValues = PaddingValues(8.dp),
            onFollowClick = onFollowClick
        )
    }
}