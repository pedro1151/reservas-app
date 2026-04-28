package com.optic.pramosreservasappz.presentation.screens.configuracion

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.components.BackTopBar

@Composable
fun MasScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: MasViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController,
                title = "Configuracion"
            )
        }
    ) { paddingValues ->
        MasContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
            navController = navController
        )
    }
}
