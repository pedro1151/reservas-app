package com.optic.pramosreservasappz.presentation.screens.clients.abmcliente

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ABMClienteScreen(
    navController: NavHostController,
    clientId: Int?,
    editable: Boolean,
) {
    Scaffold(
        topBar = {
            BackTopBar(
                title = if (editable) "Editar Cliente" else "Nuevo Cliente",
                navController = navController,
                showTitle = true
            )
        }
    ) { paddingValues ->
        ABMClienteContent(
            paddingValues = paddingValues,  // ✅ AGREGAR ESTO
            navController = navController,
            clientId = clientId,
            editable = editable
        )
    }
}
