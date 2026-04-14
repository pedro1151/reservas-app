package com.optic.pramosreservasappz.presentation.screens.salecomplete.stepone

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.components.BackTopBar
import com.optic.pramosreservasappz.presentation.components.PrimaryTopBar
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel

@Composable
fun CompleteSaleStepOneScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false,
    viewModel: SalesViewModel
) {



    Scaffold(
        topBar = {
            BackTopBar(
                title = "Nueva Venta",
                navController = navController
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


