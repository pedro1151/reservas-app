package com.optic.ecommerceappmvvm.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import com.optic.ecommerceappmvvm.presentation.ui.theme.getGreenLima

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    title: String = "",
    navController: NavController,
    showTitle: Boolean = true
) {
    TopAppBar(
        title = {
            if (showTitle) Text(
                text = title,
                color = MaterialTheme.colorScheme.primary// Texto blanco si fondo es primario
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.getGreenLima // MaterialTheme.colorScheme.onPrimary // Ícono blanco si fondo es primario
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer, // Fondo
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}