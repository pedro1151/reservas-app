package com.optic.ecommerceappmvvm.presentation.screens.prode

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
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.domain.model.League.League
import com.optic.ecommerceappmvvm.presentation.screens.prode.components.LeagueCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProdePrincipalContent(
    modifier: Modifier = Modifier,
    topLeagues: List<League>,
    leagues: List<League>,
    followedLeagues: List<League>,
    paddingValues: PaddingValues,
    viewModel: ProdeViewModel,
    navController: NavHostController,
    isAuthenticated: Boolean
) {
    val sections by viewModel.leagueSections.collectAsState()
    val query by viewModel.searchQuery.collectAsState()

    val hasQuery = query.isNotBlank()

    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // 🔍 Barra de búsqueda
        /*
        LeagueSearchBar(
            query = query,
            onQueryChange = { viewModel.onSearchQueryChanged(it) }
        )

         */

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = 1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),

            ) {

            // ---------------------------------------------------------
            // 🔥 0. SECCIÓN: LIGAS EN Q SE PARTICIPA EN PRODES — se ocultan cuando se escribe
            // ---------------------------------------------------------
            item {
                AnimatedVisibility(
                    visible = !hasQuery && sections.participateLeagues.isNotEmpty()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ligas que estas participando",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            if (!hasQuery) {
                items(
                    items = sections.participateLeagues,
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
            }

            // ---------------------------------------------------------
            // 🔥 1. SECCIÓN: LIGAS SEGUIDAS — se ocultan cuando se escribe
            // ---------------------------------------------------------
            item {
                AnimatedVisibility(
                    visible = !hasQuery && sections.followed.isNotEmpty()
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
                    items = sections.followed,
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
            }


            // ---------------------------------------------------------
            // 🔥 2 seccion -ligas MAs famosas
            // ---------------------------------------------------------
            item {
                AnimatedVisibility(
                    visible = !hasQuery && topLeagues.isNotEmpty()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ligas famosas",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            if (!hasQuery) {
                items(
                    items = sections.top,
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

            if (hasQuery && sections.explore.isEmpty()) {
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
                    items = sections.explore,
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
                                /*
                                if(isAuthenticated) {
                                    scope.launch {
                                        viewModel.createFollowedLeague(league.id)
                                    }
                                }
                                else{
                                    navController.navigate(ClientScreen.Login.route)
                                }

                                 */
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

