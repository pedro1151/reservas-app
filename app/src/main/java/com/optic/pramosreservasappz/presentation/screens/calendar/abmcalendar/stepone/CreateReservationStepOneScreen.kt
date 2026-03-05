package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.stepone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.BackTopBar
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.header.StepHeader
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.stepone.components.SelectServiceContent
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ServiceReservationStep
import com.optic.pramosreservasappz.presentation.screens.services.ServiceContent
import com.optic.pramosreservasappz.presentation.screens.services.ServiceViewModel
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateResevationStepOneScreen(
    navController: NavHostController,
    calendarViewModel: CalendarViewModel
) {
    val viewModel: ServiceViewModel = hiltViewModel()
    val serviceResource by viewModel.servicesState.collectAsState()
    val localizedContext = LocalizedContext.current

    LaunchedEffect(Unit) {
        viewModel.getServicesByProvider(1)
    }

    LaunchedEffect(Unit) {
        calendarViewModel.startReservationWizard()
    }

    var currentStep by remember { mutableStateOf(ServiceReservationStep.SELECT_SERVICE) }
    var isForward   by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController,
                title = "Seleccionar Servicio"
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // 🔥 IMPORTANTE
        ) {

            // Header con progreso
            StepHeader(currentStep = currentStep)

            when (val result = serviceResource) {
                is Resource.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                is Resource.Success -> {
                    SelectServiceContent(
                        services = result.data,
                        paddingValues = paddingValues,
                        viewModel = viewModel,
                        navController = navController,
                        localizedContext = localizedContext,
                        calendarViewModel = calendarViewModel
                    )
                }

                is Resource.Failure -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "No se pudo cargar los servicios",
                                color = Color(0xFF9E9E9E),
                                fontSize = 15.sp
                            )
                            TextButton(onClick = { viewModel.getServicesByProvider(1) }) {
                                Text("Reintentar", color = Color.Black)
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

