package com.optic.pramozventicoappz.presentation.screens.clients

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.clients.ClientResponse
import com.optic.pramozventicoappz.domain.model.product.ProductViewType
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.components.emptystate.DefaultEmptyState
import com.optic.pramozventicoappz.presentation.components.emptystate.DefaultSearchEmptyState
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.clients.components.ClientCard
import com.optic.pramozventicoappz.presentation.screens.clients.components.ClientGridCard
import com.optic.pramozventicoappz.presentation.screens.clients.components.ClientSearchRow
import kotlinx.coroutines.launch

// ─── Design Tokens ──────────────────────────────────────────────────────────────
private val Pink700  = Color(0xFFC2185B)
private val Pink600  = Color(0xFFE91E63)
private val Pink500  = Color(0xFFEC407A)
private val Pink50   = Color(0xFFFFF0F3)
private val Slate900 = Color(0xFF0F172A)
private val Slate400 = Color(0xFF94A3B8)
enum class ClientViewType { LIST, GRID }

// ─── Main Content ────────────────────────────────────────────────────────────────
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientContent(
    modifier        : Modifier = Modifier,
    clients         : List<ClientResponse>,
    paddingValues   : PaddingValues,
    viewModel       : ClientViewModel,
    navController   : NavHostController,
    isAuthenticated : Boolean = false
) {
    val query        by viewModel.searchQuery.collectAsState()
    val deleteState  by viewModel.deleteClientState
    val localClients by viewModel.localClientsList.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()
    var isDeleting        by remember { mutableStateOf(false) }
    var viewType          by remember { mutableStateOf(ProductViewType.LIST) }

    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is Resource.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Cliente eliminado ✓",
                        duration = SnackbarDuration.Short
                    )
                }
                isDeleting = false
            }
            is Resource.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Error: ${state.message}",
                        duration = SnackbarDuration.Long
                    )
                }
                isDeleting = false
            }
            is Resource.Loading -> isDeleting = true
            else                -> isDeleting = false
        }
    }

    val hasQuery         = query.isNotBlank()
    val filteredClients  = remember(query, localClients) {
        if (query.isBlank()) localClients
        else localClients.filter { client ->
            client.fullName.contains(query, ignoreCase = true) ||
                    client.email?.contains(query, ignoreCase = true) == true ||
                    client.phone?.contains(query, ignoreCase = true) == true ||
                    client.city?.contains(query, ignoreCase = true) == true ||
                    client.country?.contains(query, ignoreCase = true) == true
        }
    }

    val withPhone = remember(localClients) { localClients.count { !it.phone.isNullOrBlank() } }
    val withEmail = remember(localClients) { localClients.count { !it.email.isNullOrBlank() } }

    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (localClients.isEmpty() && !hasQuery) {
            DefaultEmptyState(
                icon = Icons.Default.Star,
                title = "Sin clientes aún",
                message = "Registra tu primer cliente.",
                buttonText = "Agregar Cliente",
                onAddClick = {
                    navController.navigate(
                        ClientScreen.ABMCliente.createRoute(clientId = null, editable = false)
                    )
                }
            )
        } else {
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 110.dp)
            ) {

                // ── Search + View Toggle ──
                item {
                    ClientSearchRow (
                        query            = query,
                        onQueryChange    = { viewModel.onSearchQueryChanged(it) },
                        hasQuery         = hasQuery,
                        totalCount       = localClients.size,
                        filteredCount    = filteredClients.size,
                        viewType         = viewType,
                        onViewTypeChange = { viewType = it }
                    )
                    Spacer(Modifier.height(10.dp))
                }

                // ── Empty search ──
                if (hasQuery && filteredClients.isEmpty()) {
                    item {
                        DefaultSearchEmptyState(
                            query = query,
                            label = "items"
                        )
                    }
                } else {
                    when (viewType) {

                        // ─ List mode ─
                        ProductViewType.LIST -> {
                            items(items = filteredClients, key = { it.id }) { client ->
                                ClientCard(
                                    client        = client,
                                    navController = navController,
                                    onDelete      = { viewModel.deleteClient(client.id) },
                                    modifier      = Modifier
                                        .animateItemPlacement(
                                            spring(
                                                Spring.DampingRatioMediumBouncy,
                                                Spring.StiffnessLow
                                            )
                                        )
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        }

                        // ─ Grid mode ─
                        ProductViewType.GRID -> {
                            val chunked = filteredClients.chunked(2)
                            items(items = chunked, key = { it.first().id }) { pair ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .animateItemPlacement(
                                            spring(
                                                Spring.DampingRatioMediumBouncy,
                                                Spring.StiffnessLow
                                            )
                                        ),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    pair.forEach { client ->
                                        ClientGridCard (
                                            client        = client,
                                            modifier      = Modifier.weight(1f),
                                            navController = navController,
                                            onDelete      = { viewModel.deleteClient(client.id) }
                                        )
                                    }
                                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                                }
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }

        // ── Snackbar ──
        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            Snackbar(
                snackbarData   = data,
                containerColor = Slate900,
                contentColor   = Color.White,
                shape          = RoundedCornerShape(14.dp)
            )
        }

        // ── Loading overlay ──
        AnimatedVisibility(
            visible  = isDeleting,
            enter    = fadeIn(),
            exit     = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(
                shape           = RoundedCornerShape(18.dp),
                color           = Color.White,
                shadowElevation = 10.dp
            ) {
                Box(Modifier.padding(22.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color       = Pink600,
                        strokeWidth = 2.5.dp,
                        modifier    = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

