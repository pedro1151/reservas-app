package com.optic.pramozventicoappz.presentation.screens.configuracion

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.presentation.components.BackTopBar
import com.optic.pramozventicoappz.presentation.components.PrimaryTopBar
import com.optic.pramozventicoappz.presentation.screens.menu.SalesScreenWithDrawer

@Composable
fun MasScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: MasViewModel = hiltViewModel()
    SalesScreenWithDrawer(navController) { onMenuClick ->
        Scaffold(
            topBar = {
                PrimaryTopBar(
                    navController = navController,
                    title = "Configuracion",
                    onMenuClick = onMenuClick
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
}
