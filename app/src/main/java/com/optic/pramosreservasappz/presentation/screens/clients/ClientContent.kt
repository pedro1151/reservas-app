package com.optic.pramosreservasappz.presentation.screens.clients

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.presentation.screens.clients.components.ClientCard

@Composable
fun ClientContent(
    modifier: Modifier = Modifier,
    clients: List<ClientResponse>,
    paddingValues: PaddingValues,
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    localizedContext: Context
) {
    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = clients,
                key = { it.id }
            ) { client ->
                ClientCard(
                    client = client,
                    navController = navController
                )
            }
        }
    }
}
