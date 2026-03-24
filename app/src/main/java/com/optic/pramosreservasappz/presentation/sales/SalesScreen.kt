package com.optic.pramosreservasappz.presentation.sales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.sales.Components.SalesContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: SalesViewModel = hiltViewModel()

    // Estado de la tab activa: 0 = Inicio, 1 = Productos
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick        = { /* TODO: navegar a crear venta */ },
                containerColor = Color(0xFF0D0D0D),
                contentColor   = Color.White,
                shape          = CircleShape,
                modifier       = Modifier.size(52.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(22.dp))
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        SalesContent(
            paddingValues = paddingValues,
            viewModel     = viewModel,
            navController = navController,
            selectedTab   = selectedTab,
            onTabChange   = { selectedTab = it }
        )
    }
}
