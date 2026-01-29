package com.optic.pramosreservasappz.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    title: String = "",
    navController: NavController,
    showTitle: Boolean = true
) {
    TopAppBar(
        title = {
            if (showTitle) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Volver",
                   // tint = MaterialTheme.colorScheme.getGreenLima // MaterialTheme.colorScheme.onPrimary // Ícono blanco si fondo es primario
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background, // Fondo
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}