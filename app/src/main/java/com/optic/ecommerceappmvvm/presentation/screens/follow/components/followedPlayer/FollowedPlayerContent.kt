package com.optic.ecommerceappmvvm.presentation.screens.follow.components.followedPlayer


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(1.dp)
        //contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // --- Sección de jugadores seguidos (usa Column interna) ---
        item {
            FollowedPlayerListContent(
                modifier = Modifier.fillMaxWidth(),
                followedPlayers = followedPlayers,
                onUnFollowClick = onUnFollowClick,
                navController = navController
            )
        }

        // Spacer entre secciones
        item { Spacer(modifier = Modifier.height(16.dp)) }

        // --- Sección de lista completa de jugadores (usa Column interna) ---
        item {
            SuggestedPlayersList(
                modifier = Modifier.fillMaxWidth(),
                players = players,
                navController = navController,
                paddingValues = PaddingValues(1.dp),
                onFollowClick = onFollowClick
            )
        }
    }
}
