package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steofour


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
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
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.header.StepHeader
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ServiceReservationStep
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import com.optic.pramosreservasappz.presentation.settings.idiomas.LocalizedContext
@Composable
fun CreateReservationStepFourScreen(
    navController: NavHostController,
    calendarViewModel: CalendarViewModel
){
    val viewModel: ClientViewModel = hiltViewModel()
    val clientResource by viewModel.clientsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadClients("", "", 1)
    }
    val localizedContext = LocalizedContext.current

    var currentStep by remember { mutableStateOf(ServiceReservationStep.SUMMARY) }
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
                title = "Resumen"
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
            ResumeContent(
                navController = navController,
                calendarViewModel = calendarViewModel
            )
        }

    }
}

