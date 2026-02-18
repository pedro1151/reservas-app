package com.optic.pramosreservasappz.presentation.screens.services

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MiscellaneousServices
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.screens.services.components.ServiceCard
import com.optic.pramosreservasappz.presentation.screens.services.components.ServiceSearchBar
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
    val deleteState by viewModel.deleteServiceState

    // 🔹 USAR LISTA LOCAL en lugar de la del servidor
    val localServices by viewModel.localServicesList.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isDeleting by remember { mutableStateOf(false) }

    // 🔹 Observar el estado de eliminación
    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is Resource.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Servicio eliminado exitosamente",
                        duration = SnackbarDuration.Short
                    )
                }
                isDeleting = false
            }
            is Resource.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Error al eliminar: ${state.message}",
                        duration = SnackbarDuration.Long
                    )
                }
                isDeleting = false
            }
            is Resource.Loading -> {
                isDeleting = true
            }
            else -> {
                isDeleting = false
            }
        }
    }

    val hasQuery = query.isNotBlank()

    // 🔹 Filtrar usando la lista LOCAL
    val filteredServices = remember(query, localServices) {
        if (query.isBlank()) {
            localServices
        } else {
            localServices.filter { service ->
                service.name.contains(query, ignoreCase = true) ||
                        service.description?.contains(query, ignoreCase = true) == true ||
                        service.category?.contains(query, ignoreCase = true) == true
            }
        }
    }

    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // 🔍 Barra de búsqueda
            ServiceSearchBar(
                query = query,
                onQueryChange = { viewModel.onSearchQueryChanged(it) }
            )

            // 📊 Contador de resultados
            AnimatedVisibility(
                visible = localServices.isNotEmpty(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (hasQuery) {
                            "${filteredServices.size} resultado${if (filteredServices.size != 1) "s" else ""}"
                        } else {
                            "${filteredServices.size} servicio${if (filteredServices.size != 1) "s" else ""}"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // 📋 Lista de servicios
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                if (hasQuery && filteredServices.isEmpty()) {
                    // Estado vacío cuando hay búsqueda sin resultados
                    item {
                        EmptySearchState()
                    }
                } else if (filteredServices.isEmpty() && !hasQuery) {
                    // Estado vacío cuando no hay servicios
                    item {
                        EmptyServicesState()
                    }
                } else {
                    items(
                        items = filteredServices,
                        key = { it.id }
                    ) { service ->
                        ServiceCard(
                            service = service,
                            navController = navController,
                            onDelete = { serviceToDelete ->
                                // 🔹 Llamar directamente a eliminar
                                viewModel.deleteService(serviceToDelete.id)
                            },
                            modifier = Modifier.animateItemPlacement(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        )
                    }

                    // Espaciador final
                    item {
                        Spacer(Modifier.height(80.dp))
                    }
                }
            }
        }

        // SnackbarHost
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        // 🔹 Loading overlay discreto para eliminación
        if (isDeleting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = MaterialTheme.shapes.large,
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Eliminando...",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF424242)
                        )
                    }
                }
            }
        }
    }
}

// 🔍 Estado vacío de búsqueda
@Composable
private fun EmptySearchState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(top = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            Text(
                text = "No se encontraron resultados",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Intenta con otro término de búsqueda",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

// 📭 Estado vacío inicial
@Composable
private fun EmptyServicesState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(top = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MiscellaneousServices,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                }
            }

            Text(
                text = "No hay servicios registrados",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Comienza agregando tu primer servicio con el botón +",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
