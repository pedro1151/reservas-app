package com.optic.ecommerceappmvvm.presentation.screens.leagues.principal

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.util.Resource
import com.optic.ecommerceappmvvm.presentation.components.ProgressBar
import androidx.compose.runtime.*
import com.optic.ecommerceappmvvm.R
import com.optic.ecommerceappmvvm.presentation.components.PrimaryTopBar
import com.optic.ecommerceappmvvm.presentation.components.progressBar.CustomProgressBar
import com.optic.ecommerceappmvvm.presentation.settings.idiomas.LocalizedContext
import com.optic.ecommerceappmvvm.presentation.ui.theme.GreyLight

@Composable
fun LeaguePrincipalScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: LeaguePrincipalViewModel = hiltViewModel()
    val leagueResource by viewModel.leaguesState.collectAsState()
    val followedLeaguesResource by viewModel.followedLeaguesListState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getLeagues()

        viewModel.getFollowedLeagues()
    }

    // para idioma
    val localizedContext = LocalizedContext.current

    Scaffold(
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = localizedContext.getString(R.string.ligas_screen_title)
            )
        },
        //containerColor = GreyLight
    ) { paddingValues ->

        val followed = (followedLeaguesResource as? Resource.Success)?.data ?: emptyList()

        when (val result = leagueResource) {
            is Resource.Loading -> {
               // CircularProgressIndicator()
            }
            is Resource.Success -> {
                LeaguePrincipalContent(
                    modifier = Modifier,
                    leagues = result.data,
                    followedLeagues = followed,
                    paddingValues = paddingValues,
                    viewModel = viewModel,
                    navController = navController,
                    isAuthenticated = isAuthenticated,
                    localizedContext = localizedContext
                )
            }
            is Resource.Failure -> {
                // mostrar error si querés
            }

            else -> {}
        }

        CustomProgressBar(isLoading = leagueResource is Resource.Loading)
    }
}