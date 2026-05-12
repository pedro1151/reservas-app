package com.optic.pramozventicoappz.presentation.screens.inicio

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.authstate.AuthStateVM
import com.optic.pramozventicoappz.presentation.components.errorstate.DefaultErrorState
import com.optic.pramozventicoappz.presentation.components.loading.DefaultLoadingState
import com.optic.pramozventicoappz.presentation.sales.SalesContent
import com.optic.pramozventicoappz.presentation.screens.menu.SalesScreenWithDrawer
import com.optic.pramozventicoappz.presentation.screens.newsale.NewSaleViewModel


@Composable
fun SalesScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    newSaleViewModel: NewSaleViewModel,
    authStateVM: AuthStateVM = hiltViewModel()
) {
    val sessionData by authStateVM.sessionData.collectAsState()

    val businessId = sessionData.businessId
    val email = sessionData.email
    val userId = sessionData.userId
    val planCode = sessionData.planCode

    val viewModel: SalesViewModel = hiltViewModel()

    // 🔥 OBSERVAR ESTADO REAL
    val salesState by viewModel.salesState.collectAsState()

    // 🔥 CARGA INICIAL
    LaunchedEffect(businessId) {
        if (businessId != null) {
            viewModel.loadSales(businessId, 25)
        }
    }

        Scaffold(
            containerColor = Color(0xFFF5F5F5)
        ) { paddingValues ->

            when (val state = salesState) {
                is Resource.Loading -> {
                    DefaultLoadingState(itemName = "ventas")
                }
                is Resource.Failure -> {

                    DefaultErrorState(
                        title = "No se pudieron cargar los ventas",
                        message = "Intenta nuevamente en unos segundos.",
                        retryText = "Volver a intentar",
                        onRetry = {
                            if (businessId != null) {
                                viewModel.loadSales(businessId, 25)
                            }
                        }
                    )

                }
                is Resource.Success -> {
                    SalesScreenWithDrawer(navController) { onMenuClick ->
                        SalesContent(
                            paddingValues = paddingValues,
                            navController = navController,
                            sales = state.data,
                            onMenuClick = onMenuClick,
                            newSaleViewModel = newSaleViewModel
                        )
                    }
                }
                else ->  DefaultLoadingState(itemName = "ventas")
            }
        }

}