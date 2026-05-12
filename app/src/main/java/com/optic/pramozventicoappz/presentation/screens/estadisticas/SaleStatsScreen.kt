package com.optic.pramozventicoappz.presentation.screens.estadisticas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.authstate.AuthStateVM
import com.optic.pramozventicoappz.presentation.components.emptystate.DefaultEmptyState
import com.optic.pramozventicoappz.presentation.components.errorstate.DefaultErrorState
import com.optic.pramozventicoappz.presentation.components.loading.DefaultLoadingState
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.estadisticas.modeanalitics.SalesStatsAnalyticsContent
import com.optic.pramozventicoappz.presentation.screens.estadisticas.modefintech.SalesStatsFintechContent
import com.optic.pramozventicoappz.presentation.screens.estadisticas.modefire.SalesStatsFireContent

@Composable
fun SalesStatsScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false ,
    authStateVM: AuthStateVM = hiltViewModel()
) {
    val sessionData by authStateVM.sessionData.collectAsState()
    // DATOS DE LA SESSION
    val businessId = sessionData.businessId
    val email = sessionData.email
    val userId = sessionData.userId
    val planCode = sessionData.planCode


    val viewModel: SaleStatsViewModel = hiltViewModel()
    val selectedMode by viewModel.selectedMode.collectAsState()

    // 🔥 OBSERVAR ESTADO REAL
    val statsState by viewModel.salesStatsState.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()

    LaunchedEffect(businessId) {
        if (businessId != null) {
            viewModel.loadStats (businessId = businessId, year = selectedYear)
        }
    }
    Scaffold(
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {

            // 🔥 CONTENIDO SIEMPRE
            // 🔥 CONTENIDO DINÁMICO SEGÚN MODO
            if (statsState is Resource.Success) {

                val data = (statsState as Resource.Success).data
                val isStatsEmpty = data.yearTotal == 0.0
                if (isStatsEmpty) {
                    DefaultEmptyState(
                        icon = Icons.Default.Bolt,
                        title = "Aún no tienes un resumen",
                        message = "Registra tu primer venta.",
                        buttonText = "Crear venta",
                        onAddClick = {
                            navController.navigate(
                                ClientScreen.CompleteSaleStepTwo.route
                            )
                        }
                    )
                }
                else {

                    when (selectedMode) {

                        SaleStatsViewModel.StatsMode.FIRE -> {
                            SalesStatsFireContent(
                                navController = navController,
                                stats = data,
                                viewModel = viewModel
                            )
                        }

                        SaleStatsViewModel.StatsMode.FINTECH -> {
                            SalesStatsFintechContent(
                                navController = navController,
                                stats = data,
                                viewModel = viewModel
                            )
                        }

                        SaleStatsViewModel.StatsMode.GAMMING -> {

                            /* REALIZAR Y REMPLAZAR POR EL MODO GAMMING ---> JHONI  */
                            // Q ES UN MODO DARK MUY COLORIDO
                            SalesStatsFintechContent(
                                navController = navController,
                                stats = data,
                                viewModel = viewModel
                            )
                        }

                        SaleStatsViewModel.StatsMode.ANALITIC -> {

                            /* REALIZAR Y REMPLAZAR POR EL MODO ALTERNATIVE ---> JHONI  */
                            SalesStatsAnalyticsContent(
                                navController = navController,
                                stats = data,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }

            // 🔥 LOADING ENCIMA (overlay)
            if (statsState is Resource.Loading) {
                DefaultLoadingState(itemName = "resumen")
            }

            // 🔥 ERROR ENCIMA
            if (statsState is Resource.Failure) {
                DefaultErrorState(
                    title = "No se pudo cargar el resumen",
                    message = "Intenta nuevamente en unos segundos.",
                    retryText = "Volver a intentar",
                    onRetry = {
                        if (businessId != null) {
                            viewModel.loadStats (businessId = businessId, year = selectedYear)
                        }
                    }
                )
            }
        }
    }

}