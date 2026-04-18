package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptree

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.optic.pramosreservasappz.presentation.screens.calendar.ActivityViewModel
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.components.BackTopBar
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.header.StepHeader
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptwo.SelectDateTimeContent
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ServiceReservationStep
import com.optic.pramosreservasappz.presentation.screens.clients.ClientContent
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCalendarStepThreeScreenReservas(
    navController: NavHostController,
    calendarViewModel: CalendarViewModel
){
    val viewModel: ClientViewModel = hiltViewModel()
    val clientResource by viewModel.clientsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadClients("", "", 1)
    }

    val localizedContext = LocalizedContext.current

    var currentStep by remember { mutableStateOf(ServiceReservationStep.SELECT_CLIENT) }
    var isForward   by remember { mutableStateOf(true) }
    var showSuccess by remember { mutableStateOf(false) }
    fun goTo(step: ServiceReservationStep, forward: Boolean = true) {
        isForward   = forward
        currentStep = step
    }

Scaffold(
    topBar = {
        BackTopBar(
            navController = navController,
            title = "Seleccionar Cliente"
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

        when (val result = clientResource) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            is Resource.Success -> {
                SelectClientContentReservas(
                    clients = result.data,
                    paddingValues = paddingValues,
                    viewModel = viewModel,
                    navController = navController,
                    calendarViewModel = calendarViewModel

                )
            }

            is Resource.Failure -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "No se pudo cargar los clientes",
                            color = Color(0xFF9E9E9E),
                            fontSize = 15.sp
                        )
                        TextButton(onClick = { viewModel.loadClients("", "", 1) }) {
                            Text("Reintentar", color = Color.Black)
                        }
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
  }
}

