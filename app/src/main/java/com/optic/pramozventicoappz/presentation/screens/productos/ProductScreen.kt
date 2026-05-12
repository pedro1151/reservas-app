package com.optic.pramozventicoappz.presentation.screens.productos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.authstate.AuthStateVM
import com.optic.pramozventicoappz.presentation.components.PrimaryTopBar
import com.optic.pramozventicoappz.presentation.components.PullRefreshWrapper
import com.optic.pramozventicoappz.presentation.components.errorstate.DefaultErrorState
import com.optic.pramozventicoappz.presentation.components.loading.DefaultLoadingState
import com.optic.pramozventicoappz.presentation.screens.menu.SalesScreenWithDrawer
import com.optic.pramozventicoappz.presentation.settings.idiomas.LocalizedContext

@Composable
fun ProductScreen(
    navController   : NavHostController,
    isAuthenticated : Boolean = false,
    authStateVM: AuthStateVM = hiltViewModel()
) {
    val sessionData by authStateVM.sessionData.collectAsState()
    // DATOS DE LA SESSION
    val businessId = sessionData.businessId

    val viewModel        : ProductViewModel = hiltViewModel()
    val productResource  by viewModel.productsState.collectAsState()
    val localizedContext = LocalizedContext.current

    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(businessId) {
        if (businessId != null) {
            viewModel.loadProducts(businessId = businessId, name = "")
        }
    }

    LaunchedEffect(productResource) {
        if (productResource !is Resource.Loading) isRefreshing = false
    }
    SalesScreenWithDrawer(navController) { onMenuClick ->
        Scaffold(
            topBar = {

                PrimaryTopBar(
                    title = "Productos y servicios",
                    navController = navController,
                    onMenuClick = onMenuClick
                )

            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->

            PullRefreshWrapper(
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    viewModel.loadProducts(businessId = 1, name = "")
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val result = productResource) {
                    is Resource.Loading -> DefaultLoadingState(itemName = "productos")
                    is Resource.Success -> {
                        ProductContent(
                            products = result.data,
                            paddingValues = PaddingValues(0.dp),
                            viewModel = viewModel,
                            navController = navController,
                            isAuthenticated = isAuthenticated,
                            localizedContext = localizedContext
                        )
                    }

                    is Resource.Failure -> {

                        DefaultErrorState(
                            title = "No se pudieron cargar los clientes",
                            message = "Intenta nuevamente en unos segundos.",
                            retryText = "Volver a intentar",
                            onRetry = {
                                isRefreshing = true
                                if (businessId != null) {
                                    viewModel.loadProducts(businessId = businessId, name = "")
                                }
                            }
                        )
                    }

                    else -> DefaultLoadingState(itemName = "productos")
                }
            }
        }
    }
}


