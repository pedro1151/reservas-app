package com.optic.ecommerceappmvvm.presentation.screens.follow.components.followedTeam


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.Team


@Composable
fun FollowedTeamContent(
    teams: List<Team>,
    followedTeams: List<Team>,
    navController: NavHostController,
    onFollowClick: (Int) -> Unit = {},
    onUnFollowClick: (Int) -> Unit = {},
    paddingValues: PaddingValues
) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize(),
          //  .padding(paddingValues),
        verticalArrangement = Arrangement.Top
    ) {

        item {
            FollowedTeamListContent(
                modifier = Modifier.fillMaxWidth(),
                followedTeams = followedTeams,
                onUnFollowClick = onUnFollowClick,
                navController = navController
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            TeamListContent(
                modifier = Modifier.fillMaxWidth(),
                teams = teams,
                navController = navController,
                paddingValues = PaddingValues(1.dp),
                onFollowClick = onFollowClick
            )
        }
    }
}
