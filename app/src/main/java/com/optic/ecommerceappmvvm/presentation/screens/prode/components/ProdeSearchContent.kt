package com.optic.ecommerceappmvvm.presentation.screens.prode.components

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
import com.optic.ecommerceappmvvm.presentation.navigation.screen.client.ClientScreen
import com.optic.ecommerceappmvvm.presentation.screens.prode.ProdeViewModel
import com.optic.ecommerceappmvvm.presentation.screens.prode.components.LeagueCard
import com.optic.ecommerceappmvvm.presentation.screens.prode.components.LeagueSearchBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProdeSearchContent(
    modifier: Modifier = Modifier,
    leagues: List<League>,
    followedLeagues: List<League>,
    paddingValues: PaddingValues,
    viewModel: ProdeViewModel,
    navController: NavHostController,
    isAuthenticated: Boolean
) {

    val query by viewModel.searchQuery.collectAsState()

    val hasQuery = query.isNotBlank()

    val filteredLeagues = remember(query, leagues) {
        leagues.filter { league ->
            league.name.contains(query, ignoreCase = true) ||
                    league.type.contains(query, ignoreCase = true) ||
                    (league.country?.name?.contains(query, ignoreCase = true) ?: false)
        }
    }

    val remainingLeagues = filteredLeagues.filterNot { fl ->
        followedLeagues.any { it.id == fl.id }
    }

    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // 🔍 Barra de búsqueda
        LeagueSearchBar(
            query = query,
            onQueryChange = { viewModel.onSearchQueryChanged(it) }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = 1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),

            ) {

            // ---------------------------------------------------------
            // 🔥 1. SECCIÓN: LIGAS SEGUIDAS — se ocultan cuando se escribe
            // ---------------------------------------------------------
            item {
                AnimatedVisibility(
                    visible = !hasQuery && followedLeagues.isNotEmpty()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ligas que sigues",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            if (!hasQuery) {
                items(
                    items = followedLeagues,
                    key = { it.id }
                ) { league ->
                    AnimatedVisibility(
                        visible = true,
                        modifier = Modifier.animateItemPlacement()
                    ) {
                        val scope = rememberCoroutineScope()
                        LeagueCard(
                            league = league,
                            isFollowed = true,
                            onFollowClick = {
                            },
                            navController = navController
                        )
                    }
                }

                item {
                    AnimatedVisibility(visible = true) {
                        Column {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Explorar más ligas",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            // ---------------------------------------------------------
            // 🔥 2. RESULTADOS DE BÚSQUEDA
            // ---------------------------------------------------------

            if (hasQuery && filteredLeagues.isEmpty()) {
                item {
                    AnimatedVisibility(visible = true) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 60.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text(
                                text = "No se encontraron coincidencias",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            } else {
                items(
                    items = remainingLeagues,
                    key = { it.id }
                ) { league ->
                    AnimatedVisibility(
                        visible = true,
                        modifier = Modifier.animateItemPlacement()
                    ) {
                        val scope = rememberCoroutineScope()
                        LeagueCard(
                            league = league,
                            isFollowed = false,
                            onFollowClick = {
                                if(isAuthenticated) {
                                    scope.launch {
                                        viewModel.createFollowedLeague(league.id)
                                    }
                                }
                                else{
                                    navController.navigate(ClientScreen.Login.route)
                                }
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

