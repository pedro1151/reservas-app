package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.PrimaryTopBar

@Composable
fun ClientPrincipalScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: ClientViewModel = hiltViewModel()
    val clientResource by viewModel.clientsState.collectAsState()

    Scaffold(
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = "Clientes"
            )
        }
    ) { paddingValues ->
        when (val result = clientResource) {
            is Resource.Loading -> {
                // Mostrar loading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Success -> {
                ClientContent(
                    clients = result.data,
                    paddingValues = paddingValues,
                    viewModel = viewModel,
                    navController = navController
                )
            }

            is Resource.Failure -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Text("Error: ${result.message}")
                }
            }

            else -> {
                // Estado por defecto
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}