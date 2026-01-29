package com.optic.pramosreservasappz.presentation.screens.clients

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.PrimaryTopBar
import com.optic.pramosreservasappz.presentation.components.progressBar.CustomProgressBar
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext

@Composable
fun ClientPrincipalScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: ClientViewModel = hiltViewModel()
    val clientResource by viewModel.clientsState.collectAsState()

    val localizedContext = LocalizedContext.current

    Scaffold(
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = "Clientes"
            )
        }
    ) { paddingValues ->

        when (val result = clientResource) {
            is Resource.Loading -> Unit

            is Resource.Success -> {
                ClientContent(
                    modifier = Modifier,
                    clients = result.data,
                    paddingValues = paddingValues,
                    navController = navController,
                    isAuthenticated = isAuthenticated,
                    localizedContext = localizedContext
                )
            }

            is Resource.Failure -> {
                // manejar error si querés
            }

            else -> Unit
        }

        CustomProgressBar(isLoading = clientResource is Resource.Loading)
    }
}
