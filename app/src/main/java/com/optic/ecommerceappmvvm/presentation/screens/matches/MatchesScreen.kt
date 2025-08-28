package com.optic.ecommerceappmvvm.presentation.screens.matches


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

import androidx.compose.runtime.*
import androidx.navigation.compose.currentBackStackEntryAsState
import com.optic.ecommerceappmvvm.presentation.components.PrimaryTopBar
import com.optic.ecommerceappmvvm.presentation.screens.fixtures.list.FixtureList
import com.optic.ecommerceappmvvm.presentation.screens.matches.folllowfixtures.FollowFixtureList
import com.optic.ecommerceappmvvm.presentation.ui.theme.GreyLight


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchesScreen(navController: NavHostController) {
    val viewModel: MatchesViewModel = hiltViewModel()

    val fixtureState by viewModel.fixtureTeamsState.collectAsState()

    // Llamamos la función cuando entra la pantalla
    val backStackEntry = navController.currentBackStackEntryAsState().value
    LaunchedEffect(backStackEntry?.destination?.route) {
        viewModel.getFixtureFollowedTeams(season = 2023, date = "2023-09-17")
    }

    Scaffold(
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = "SMARTF"
            )
        },
        containerColor = GreyLight
    ) { paddingValues ->
        FollowFixtureList(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            fixtureState = fixtureState
        )
    }
}