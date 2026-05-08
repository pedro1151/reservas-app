package com.optic.pramozventicoappz.presentation.screens.tusventas.detail

import com.optic.pramozventicoappz.presentation.screens.inicio.SalesViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.components.BackTopBar

@Composable
fun SaleDetailScreen(
    navController: NavHostController,
    saleId: Int,
    isAuthenticated: Boolean = false
) {
    val viewModel: SalesViewModel = hiltViewModel()

    val oneSaleState by viewModel.oneSaleState

    LaunchedEffect(saleId) {
        viewModel.getSaleById(saleId)
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title        = "Detalle de la Venta",
                navController = navController
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        when (val state = oneSaleState) {

            is Resource.Loading -> {
                Box(
                    modifier         = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black, strokeWidth = 2.dp)
                }
            }

            is Resource.Failure -> {
                Box(
                    modifier         = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se pudo cargar la venta")
                }
            }

            is Resource.Success -> {
                // Se pasa paddingValues para que el LazyColumn lo use como contentPadding
                // y el contenido no quede oculto bajo el TopBar
                SaleDetailContent(
                    sale          = state.data,
                    paddingValues = paddingValues
                )
            }

            else -> {}
        }
    }
}
