package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.presentation.screens.clients.components.ClientCard
import com.optic.pramosreservasappz.presentation.screens.clients.components.ClientSearchBar

@Composable
fun ClientContent(
    modifier: Modifier = Modifier,
    clients: List<ClientResponse>,
    paddingValues: PaddingValues,
    viewModel: ClientViewModel,
    navController: NavHostController
) {

    val query by viewModel.searchQuery.collectAsState()

    val hasQuery = query.isNotBlank()

    val filteredClients = remember(query, clients) {
        clients.filter { client ->
            client.fullName.contains(query, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        ClientSearchBar(
            query = query,
            onQueryChange = { viewModel.onSearchQueryChanged(it) }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {

            if (hasQuery && filteredClients.isEmpty()) {
                item {
                    AnimatedVisibility(true) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se encontraron coincidencias")
                        }
                    }
                }
            } else {
                items(filteredClients, key = { it.id }) { client ->
                    ClientCard(client = client, navController = navController)
                }
            }
        }
    }
}
