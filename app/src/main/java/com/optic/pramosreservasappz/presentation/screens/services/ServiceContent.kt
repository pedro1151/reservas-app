package com.optic.pramosreservasappz.presentation.screens.services

import android.content.Context
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
import com.optic.pramosreservasappz.R
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse

import com.optic.pramosreservasappz.presentation.screens.services.components.ServiceCard
import com.optic.pramosreservasappz.presentation.screens.services.components.ServiceSearchBar
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServiceContent(
    modifier: Modifier = Modifier,
    services: List<ServiceResponse>,
    paddingValues: PaddingValues,
    viewModel: ServiceViewModel,
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    localizedContext: Context
) {

    val query by viewModel.searchQuery.collectAsState()

    val hasQuery = query.isNotBlank()

    val filteredServices = remember(query, services) {
        services.filter { service ->
            service.name.contains(query, ignoreCase = true)
        }
    }


    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // 🔍 Barra de búsqueda
        ServiceSearchBar(
            query = query,
            onQueryChange = { viewModel.onSearchQueryChanged(it) }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = paddingValues.calculateBottomPadding()),
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),

        ) {


            // ---------------------------------------------------------
            // 🔥 1. RESULTADOS DE BÚSQUEDA
            // ---------------------------------------------------------

            if (hasQuery && filteredServices.isEmpty()) {
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
                    items = filteredServices,
                    key = { it.id }
                ) { service ->
                    AnimatedVisibility(
                        visible = true,
                        modifier = Modifier.animateItemPlacement()
                    ) {
                        val scope = rememberCoroutineScope()
                        ServiceCard(
                            service = service,
                            isFollowed = false,
                            onFollowClick = {
                                /*
                                    scope.launch {
                                        viewModel.createFollowedLeague(league.id, isAuthenticated)
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

