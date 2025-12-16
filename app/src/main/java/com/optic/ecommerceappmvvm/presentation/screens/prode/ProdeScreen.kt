package com.optic.ecommerceappmvvm.presentation.screens.prode

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import com.optic.ecommerceappmvvm.presentation.components.PrimaryTopBar
import com.optic.ecommerceappmvvm.presentation.components.progressBar.CustomProgressBar
import com.optic.ecommerceappmvvm.presentation.screens.prode.buttons.ProdeTopBar
import com.optic.ecommerceappmvvm.presentation.screens.prode.components.ProdeOptionsList
import com.optic.ecommerceappmvvm.presentation.screens.prode.components.ProdeSearchContent
import com.optic.ecommerceappmvvm.presentation.ui.theme.GreyLight

@Composable
fun ProdeScreen(
    navController: NavHostController,
    isAuthenticated: Boolean
) {
    val viewModel: ProdeViewModel = hiltViewModel()

    val leagueResource by viewModel.leaguesState.collectAsState()
    val leaguesTop by viewModel.leaguesTopState.collectAsState()
    val followedLeaguesResource by viewModel.followedLeaguesListState.collectAsState()


    val listState = rememberLazyListState()
    val isTopBarVisible by rememberTopBarScrollBehavior(listState)

    val topBarOffset by animateDpAsState(
        targetValue = if (isTopBarVisible) 0.dp else (-96).dp,
        animationSpec = tween(durationMillis = 220),
        label = "topBarOffset"
    )
    LaunchedEffect(Unit) {
        viewModel.getTopLeagues()
        viewModel.getFollowedLeagues()
    }

    Scaffold(
        topBar = {
            PrimaryTopBar(
                title = "Prodes",
                navController = navController
            )
        }
    ) { paddingValues ->

        val followed = (followedLeaguesResource as? Resource.Success)?.data ?: emptyList()
        val leagues  = (leagueResource as? Resource.Success)?.data ?: emptyList()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // 👈 padding SOLO aquí
        ) {

            // 🔗 LINKS / OPCIONES (FIJO DEBAJO DEL TOPBAR)
            ProdeOptionsList(
                modifier = Modifier,
                onOptionClick = { index ->
                    when (index) {
                        0 -> {
                            // Ranking
                        }
                        1 -> {
                            // Ver prodes
                        }
                        2 -> {
                            // Option 3
                        }
                    }
                }
            )
            // 👇 CONTENIDO SCROLLEABLE
            when (val result = leaguesTop) {
                is Resource.Loading -> {
                    // Loader opcional
                }

                is Resource.Success -> {
                    ProdeSearchContent(
                        modifier = Modifier.weight(1f), // 👈 ocupa el resto
                        topLeagues = result.data,
                        leagues = leagues,
                        followedLeagues = followed,
                        paddingValues = PaddingValues(0.dp), // 👈 IMPORTANTE
                        viewModel = viewModel,
                        navController = navController,
                        isAuthenticated = isAuthenticated
                    )
                }

                is Resource.Failure -> {
                    // error UI
                }

                else -> {}
            }
        }

        CustomProgressBar(isLoading = leagueResource is Resource.Loading)
    }
}



@Composable
fun rememberTopBarScrollBehavior(
    listState: LazyListState
): State<Boolean> {
    val isTopBarVisible = remember { mutableStateOf(true) }

    var lastIndex by remember { mutableStateOf(0) }
    var lastScrollOffset by remember { mutableStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->

            val scrollingDown =
                index > lastIndex ||
                        (index == lastIndex && offset > lastScrollOffset)

            if (scrollingDown) {
                isTopBarVisible.value = false
            } else {
                isTopBarVisible.value = true
            }

            lastIndex = index
            lastScrollOffset = offset
        }
    }

    return isTopBarVisible
}

