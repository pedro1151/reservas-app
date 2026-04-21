package com.optic.pramosreservasappz.presentation.screens.recibos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.BackTopBar

@Composable
fun ReciboScreen(
    navController: NavHostController,
    saleId: Int,
    isAuthenticated: Boolean = false
) {

    val viewModel: ReciboViewModel = hiltViewModel()

    val oneSaleState by viewModel.oneSaleState
    val businessState by viewModel.businessState

    LaunchedEffect(saleId) {
        viewModel.getSaleById(saleId)
        viewModel.getBusinessById(1, 1)
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = "Recibo",
                navController = navController
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        when (val state = oneSaleState) {

            is Resource.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                }
            }

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

            is Resource.Success -> {

                // 🔥 obtener business solo si cargó correctamente
                val businessData: BusinessCompleteResponse? =
                    when (val bState = businessState) {
                        is Resource.Success -> bState.data
                        else -> null
                    }

                ReciboContent(
                    sale = state.data,
                    business = businessData,
                    paddingValues = paddingValues
                )
            }

            else -> {}
        }
    }
}