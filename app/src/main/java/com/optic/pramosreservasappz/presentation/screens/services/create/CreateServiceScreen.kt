package com.optic.pramosreservasappz.presentation.screens.services.create


import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController

import com.optic.pramosreservasappz.presentation.components.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateServiceScreen(
    navController: NavHostController,
    serviceId: Int?,
    editable: Boolean,
) {
    Scaffold(
        topBar = {
            BackTopBar(
                title = if (editable) "Editar Servicio" else "Nuevo Servicio",
                navController = navController,
                showTitle = true
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CreateServiceContent(
                navController = navController,
                serviceId =  serviceId,
                editable = editable
            )
        }
    }
}
