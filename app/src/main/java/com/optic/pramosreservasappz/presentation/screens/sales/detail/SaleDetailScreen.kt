package com.optic.pramosreservasappz.presentation.screens.sales.detail

import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.BackTopBar
import com.optic.pramosreservasappz.presentation.sales.Components.SalesContent

@Composable
fun SaleDetailScreen(
    navController: NavHostController,
    saleId: Int,
    isAuthenticated: Boolean = false
) {
    val viewModel: SalesViewModel = hiltViewModel()
    // cerrar abrir menu
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope       = rememberCoroutineScope()

    // Estado de la tab activa
    var selectedTab by remember { mutableStateOf(0) }

    // 🔥 OBSERVAR ESTADO REAL
    val oneSaleState by viewModel.oneSaleState

    // 🔥 CARGA INICIAL
    LaunchedEffect(saleId) {
        viewModel.getSaleById(saleId)
    }

        Scaffold(

            topBar = {
                BackTopBar(
                    title = "DETALLE VENTA",
                    navController=navController
                )
            },
            containerColor = Color(0xFFF5F5F5)
        ) { paddingValues ->

            when (val state = oneSaleState) {

                // 🔄 LOADING
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                // ❌ ERROR
                is Resource.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No se pudo cargar la venta")
                    }
                }

                // ✅ SUCCESS
                is Resource.Success -> {
                    SaleDetailContent(
                        sale = state.data
                    )
                }

                else -> {}
            }
        }

}