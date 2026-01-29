package com.optic.pramosreservasappz.presentation.screens.services

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.optic.pramosreservasappz.presentation.components.PrimaryTopBar
import com.optic.pramosreservasappz.presentation.components.progressBar.CustomProgressBar
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext

@Composable
fun ServiceScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: ServiceViewModel = hiltViewModel()
    val serviceResource by viewModel.servicesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getServicesByProvider(1)
    }

    // para idioma
    val localizedContext = LocalizedContext.current

    Scaffold(
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = "Servicios"
            )
        }

    ) { paddingValues ->

        when (val result = serviceResource) {
            is Resource.Loading -> {
                // CircularProgressIndicator()
            }
            is Resource.Success -> {
                ServiceContent(
                    modifier = Modifier,
                    services = result.data,
                    paddingValues = paddingValues,
                    viewModel = viewModel,
                    navController = navController,
                    isAuthenticated = isAuthenticated,
                    localizedContext = localizedContext
                )
            }
            is Resource.Failure -> {
                // mostrar error si querés
            }
            else -> {}
        }

        CustomProgressBar(isLoading = serviceResource is Resource.Loading)
    }
}
