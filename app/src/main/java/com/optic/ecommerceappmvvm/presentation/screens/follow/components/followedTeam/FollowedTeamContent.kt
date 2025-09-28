package com.optic.ecommerceappmvvm.presentation.screens.follow.components.followedTeam


import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.Team
import com.optic.ecommerceappmvvm.presentation.screens.team.list.TeamListContent


@Composable
fun FollowedTeamContent(
    teams: List<Team>,
    followedTeams: List<Team>,
    navController: NavHostController,
    onFollowClick: (Int) -> Unit = {},
    onUnFollowClick: (Int) -> Unit = {},
    paddingValues: PaddingValues
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {

        // Parte superior: jugadores seguidos
        FollowedTeamListContent(
            modifier = Modifier,
            followedTeams = followedTeams,
            onUnFollowClick = onUnFollowClick,
            navController = navController
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Parte inferior: lista completa de jugadores
        TeamListContent(
            modifier = Modifier.fillMaxSize(),
            teams = teams,
            navController = navController,
            paddingValues = PaddingValues(8.dp),
             onFollowClick = onFollowClick
        )
    }
}