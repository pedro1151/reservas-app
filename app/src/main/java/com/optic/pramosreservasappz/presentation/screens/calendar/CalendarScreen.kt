package com.optic.pramosreservasappz.presentation.screens.calendar

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import androidx.compose.runtime.*
import com.optic.pramosreservasappz.presentation.components.PrimaryTopBar
import com.optic.pramosreservasappz.presentation.components.progressBar.CustomProgressBar
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext

@Composable
fun CalendarScreen(
    navController: NavHostController,
    isAuthenticated: Boolean = false
) {
    val viewModel: CalendarViewModel = hiltViewModel()
    val clientResource by viewModel.clientsState.collectAsState()

    val staffResource by viewModel.staffState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getStaffTotales()
        viewModel.getClientsByProvider(1, "", "")
    }

    // para idioma
    val localizedContext = LocalizedContext.current

    Scaffold(
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = "Calendario"
            )
        },
        //containerColor = GreyLight
    ) { paddingValues ->

        val clients = (clientResource as? Resource.Success)?.data ?: emptyList()
        when (val result = staffResource) {
            is Resource.Loading -> {
               // CircularProgressIndicator()
            }
            is Resource.Success -> {
                CalendarContent(
                    modifier = Modifier,
                    staff = result.data,
                    clients = clients,
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

        CustomProgressBar(isLoading = clientResource is Resource.Loading)
    }
}