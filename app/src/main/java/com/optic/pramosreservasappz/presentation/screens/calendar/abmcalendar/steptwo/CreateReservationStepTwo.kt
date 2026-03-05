package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptwo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.optic.pramosreservasappz.presentation.screens.calendar.ActivityViewModel
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.components.BackTopBar
import com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.header.StepHeader
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ServiceReservationStep
import java.time.LocalDate



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReservationStepTwoScreen(
    navController: NavHostController,
    calendarViewModel: CalendarViewModel
) {
    var currentStep by remember { mutableStateOf(ServiceReservationStep.SELECT_SERVICE) }
    var isForward   by remember { mutableStateOf(true) }
    var showSuccess by remember { mutableStateOf(false) }
    fun goTo(step: ServiceReservationStep, forward: Boolean = true) {
        isForward   = forward
        currentStep = step
    }

    val selectedServiceId by calendarViewModel.selectedServiceIdForReservation.collectAsState()
    val selectedClientId  by calendarViewModel.selectedClientIdForReservation.collectAsState()
    val selectedDate      by calendarViewModel.selectedDateForReservation.collectAsState()
    val selectedTime      by calendarViewModel.selectedTimeForReservation.collectAsState()


    Scaffold(
        topBar = {
            BackTopBar(
                navController = navController,
                title = "Seleccionar Horario"
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
            StepHeader(currentStep = currentStep )

            SelectDateTimeContent(
                initialDate = selectedDate ?: LocalDate.now(),
                initialTime = selectedTime,
                onConfirm = { date, time ->
                    calendarViewModel.setSelectedDateForReservation(date)
                    calendarViewModel.setSelectedTimeForReservation(time)
                    goTo(ServiceReservationStep.SELECT_CLIENT)
                },
                navController = navController,
                calendarViewModel = calendarViewModel
            )

        }
    }
}

