package com.optic.pramozventicoappz.presentation.screens.clients

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.authstate.AuthStateVM
import com.optic.pramozventicoappz.presentation.components.BackTopBar
import com.optic.pramozventicoappz.presentation.components.PrimaryTopBar
import com.optic.pramozventicoappz.presentation.components.PullRefreshWrapper
import com.optic.pramozventicoappz.presentation.components.errorstate.DefaultErrorState
import com.optic.pramozventicoappz.presentation.components.loading.DefaultLoadingState
import com.optic.pramozventicoappz.presentation.screens.menu.SalesScreenWithDrawer

// ─── Design Tokens ─────────────────────────────────────────────────────────────

@Composable
fun ClientPrincipalScreen(
    navController   : NavHostController,
    isAuthenticated : Boolean = false,
    authStateVM: AuthStateVM = hiltViewModel()
) {
    val sessionData by authStateVM.sessionData.collectAsState()
    // DATOS DE LA SESSION
    val businessId = sessionData.businessId
    val email = sessionData.email
    val userId = sessionData.userId
    val planCode = sessionData.planCode



    val viewModel      : ClientViewModel = hiltViewModel()
    val clientResource by viewModel.clientsState.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(businessId) {
        if (businessId != null) {
            viewModel.loadClients("", "", businessId)
        }
    }

    LaunchedEffect(clientResource) {
        if (clientResource !is Resource.Loading) isRefreshing = false
    }
    SalesScreenWithDrawer(navController) { onMenuClick ->
        Scaffold(
            topBar = {
                BackTopBar(
                    title = "Tus clientes",
                    navController = navController
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->

            PullRefreshWrapper(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    viewModel.loadClients("", "", 1)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val result = clientResource) {
                    is Resource.Loading -> DefaultLoadingState(itemName = "clientes")
                    is Resource.Success -> {
                        ClientContent(
                            clients = result.data,
                            paddingValues = PaddingValues(0.dp),
                            viewModel = viewModel,
                            navController = navController,
                            isAuthenticated = isAuthenticated
                        )
                    }

                    is Resource.Failure -> {

                        DefaultErrorState(
                            title = "No se pudieron cargar los clientes",
                            message = "Intenta nuevamente en unos segundos.",
                            retryText = "Volver a intentar",
                            onRetry = {
                                isRefreshing = true
                                viewModel.loadClients("", "", 1)
                            }
                        )

                    }

                    else -> DefaultLoadingState(itemName = "clientes")
                }
            }
        }
    }
}



