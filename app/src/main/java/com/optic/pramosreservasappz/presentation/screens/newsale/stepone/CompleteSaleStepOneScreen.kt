package com.optic.pramosreservasappz.presentation.screens.newsale.stepone

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.components.BackTopBar
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.inicio.SalesViewModel
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel

@Composable
fun CompleteSaleStepOneScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    viewModel: NewSaleViewModel
) {



    Scaffold(
        topBar = {
            BackTopBar(
                title = "Nueva Venta",
                navController = navController,
                back = ClientScreen.Sales.route // volver al home de sales
            )
        }
    ) { paddingValues ->

        // LISTA DE RESUMEN DE PRODUCTOS
        CompleteSalePaso1Content(
            viewModel = viewModel,
            navController = navController,
            isAuthenticated = isAuthenticated
        )
    }

}


