package com.optic.pramosfootballappz.presentation.screens.prode

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosfootballappz.domain.util.Resource
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.optic.pramosfootballappz.presentation.components.progressBar.CustomProgressBar
import com.optic.pramosfootballappz.presentation.screens.prode.topbar.PrincipalProdeTopBar
import com.optic.pramosfootballappz.presentation.settings.idiomas.LocalizedContext
import com.optic.pramosfootballappz.R

@Composable
fun ProdeScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: ProdeViewModel = hiltViewModel()

    // para idioma
    val localizedContext = LocalizedContext.current

    val leagueResource by viewModel.leaguesState.collectAsState()
    val leaguesParticipateResource by viewModel.leaguesParticipateState.collectAsState()
    val leaguesTop by viewModel.leaguesTopState.collectAsState()
    val ranking by viewModel.ranking.collectAsState()
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
        viewModel.getFollowedLeagues(isAuthenticated)
        viewModel.getPredictionRanking(10)
        viewModel.getProdeParticipateLeagues(-1)  //-1 representa el usuario NO logueado, recupera desde cache
    }

    Scaffold(
        topBar = {
            PrincipalProdeTopBar(
                title = localizedContext.getString(R.string.prode_screen_title),
                navController = navController,
                prodeViewModel = viewModel
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
            /*
            ProdeOptionsList(
                modifier = Modifier,
                onOptionClick = { index ->
                    when (index) {
                        0 -> {
                            navController.navigate(ClientScreen.ProdeRanking.route) {
                                popUpTo(0) // elimina TODA la pila
                                launchSingleTop = true
                            }
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

             */
            // 👇 CONTENIDO SCROLLEABLE
            when (val result = leaguesTop) {
                is Resource.Loading -> {
                    // Loader opcional
                }

                is Resource.Success -> {
                    ProdePrincipalContent(
                        modifier = Modifier.weight(1f), // 👈 ocupa el resto
                        topLeagues = result.data,
                        leagues = leagues,
                        followedLeagues = followed,
                        paddingValues = PaddingValues(0.dp), // 👈 IMPORTANTE
                        viewModel = viewModel,
                        navController = navController,
                        isAuthenticated = isAuthenticated,
                        localizedContext = localizedContext
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

