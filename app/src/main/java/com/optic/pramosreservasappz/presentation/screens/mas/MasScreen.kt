package com.optic.pramosreservasappz.presentation.screens.mas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.components.PrimaryTopBar

@Composable
fun MasScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: MasViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = "Más"
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
