package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.clients.components.ClientCard
import com.optic.pramosreservasappz.presentation.screens.clients.components.ClientSearchBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientContent(
    modifier: Modifier = Modifier,
    clients: List<ClientResponse>,
    paddingValues: PaddingValues,
    viewModel: ClientViewModel,
    navController: NavHostController
) {
    val query by viewModel.searchQuery.collectAsState()
    val deleteState by viewModel.deleteClientState
    val localClients by viewModel.localClientsList.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isDeleting by remember { mutableStateOf(false) }

    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is Resource.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Cliente eliminado", duration = SnackbarDuration.Short)
                }
                isDeleting = false
            }
            is Resource.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Error: ${state.message}", duration = SnackbarDuration.Long)
                }
                isDeleting = false
            }
            is Resource.Loading -> isDeleting = true
            else -> isDeleting = false
        }
    }

    val hasQuery = query.isNotBlank()
    val filteredClients = remember(query, localClients) {
        if (query.isBlank()) localClients
        else localClients.filter { client ->
            client.fullName.contains(query, ignoreCase = true) ||
                    client.email?.contains(query, ignoreCase = true) == true ||
                    client.phone?.contains(query, ignoreCase = true) == true ||
                    client.city?.contains(query, ignoreCase = true) == true ||
                    client.country?.contains(query, ignoreCase = true) == true
        }
    }

    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (localClients.isEmpty() && !hasQuery) {
            EmptyClientsState(
                onAddClient = {
                    navController.navigate(
                        ClientScreen.ABMCliente.createRoute(clientId = null, editable = false)
                    )
                }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {

                // ── Search bar + contador integrado ──
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Campo de búsqueda ocupa el espacio disponible
                    ClientSearchBar(
                        query = query,
                        onQueryChange = { viewModel.onSearchQueryChanged(it) },
                        modifier = Modifier.weight(1f)
                    )

                    // Contador sutil a la derecha — solo visible si no hay búsqueda activa
                    if (!hasQuery && localClients.isNotEmpty()) {
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "${localClients.size}",
                            fontSize = 12.sp,
                            color = Color(0xFFBBBBBB),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }

                // Lista
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 80.dp
                    )
                ) {
                    if (hasQuery && filteredClients.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.Search,
                                        null,
                                        tint = Color(0xFFDDDDDD),
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Text(
                                        "Sin resultados para \"$query\"",
                                        fontSize = 13.sp,
                                        color = Color(0xFFBBBBBB)
                                    )
                                }
                            }
                        }
                    } else {
                        items(
                            items = filteredClients,
                            key = { it.id }
                        ) { client ->
                            ClientCard(
                                client = client,
                                navController = navController,
                                onDelete = { viewModel.deleteClient(client.id) },
                                modifier = Modifier.animateItemPlacement(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
        }

        AnimatedVisibility(
            visible = isDeleting,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(
                color = Color.Black,
                strokeWidth = 2.dp,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun EmptyClientsState(onAddClient: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            Text(
                "No hay clientes para mostrar.",
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                letterSpacing = (-0.2).sp
            )
            Spacer(Modifier.height(4.dp))
            Button(
                onClick = onAddClient,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Añadir nuevo cliente", fontSize = 15.sp, color = Color.White)
            }
            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Importar desde contactos", fontSize = 15.sp, color = Color.Black)
            }
        }
    }
}
