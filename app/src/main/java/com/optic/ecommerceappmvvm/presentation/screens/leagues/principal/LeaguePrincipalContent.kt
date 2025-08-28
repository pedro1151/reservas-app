package com.optic.ecommerceappmvvm.presentation.screens.leagues.principal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.presentation.screens.leagues.principal.components.LeagueCard
import com.optic.ecommerceappmvvm.presentation.screens.leagues.principal.components.LeagueSearchBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
// CONTENIDO PRINCIPAL
fun LeaguePrincipalContent(
    modifier: Modifier = Modifier,
    leagues: List<League>,
    followedLeagues: List<League>,
    paddingValues: PaddingValues,
    viewModel: LeaguePrincipalViewModel,
    navController: NavHostController
) {
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(400)
            .distinctUntilChanged()
            .collectLatest { value ->
                if (value.isBlank()) {
                    viewModel.getLeagues()
                } else {
                    viewModel.getLeagues(
                        name = value,
                        type = value,
                        countryName = value
                    )
                }
            }
    }

    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LeagueSearchBar(
            query = query,
            onQueryChange = { query = it }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (followedLeagues.isNotEmpty()) {
                item {
                    Text(
                        text = "Ligas que sigues",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }

                items(
                    items = followedLeagues,
                    key = { it.id }
                ) { league ->
                    var visible by remember { mutableStateOf(true) }

                    LaunchedEffect(league) {
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        modifier = Modifier.animateItemPlacement()
                    ) {
                        val coroutineScope = rememberCoroutineScope()
                        LeagueCard(
                            league = league,
                            isFollowed = true,
                            onFollowClick = {
                            visible = false
                            coroutineScope.launch {
                                delay(200)
                                viewModel.deleteFollowedLeague(league.id)
                            }
                        },
                            navController = navController
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Explorar más ligas",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            val remainingLeagues = leagues.filterNot { l -> followedLeagues.any { it.id == l.id } }

            items(
                items = remainingLeagues,
                key = { it.id }
            ) { league ->
                var visible by remember { mutableStateOf(true) }

                LaunchedEffect(league) {
                    visible = true
                }

                AnimatedVisibility(
                    visible = visible,
                    modifier = Modifier.animateItemPlacement()
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    LeagueCard(
                        league = league,
                        isFollowed = false,
                        onFollowClick = {
                            visible = false
                            coroutineScope.launch {
                                delay(200)
                                viewModel.createFollowedLeague(league.id)
                            }
                        },
                        navController = navController
                    )
                }
            }
        }
    }
}
