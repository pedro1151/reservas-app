package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.optic.pramosreservasappz.domain.model.reservas.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservas.services.ServiceResponse
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import com.optic.pramosreservasappz.presentation.screens.services.ServiceViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

enum class ReservationStep {
    SELECT_SERVICE,
    SELECT_DATETIME,
    SELECT_CLIENT,
    SUMMARY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceReservationWizard(
    onDismiss: () -> Unit,
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    serviceViewModel: ServiceViewModel = hiltViewModel(),
    clientViewModel: ClientViewModel = hiltViewModel()
) {
    var currentStep by remember { mutableStateOf(ReservationStep.SELECT_SERVICE) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Estados
    val selectedServiceId by calendarViewModel.selectedServiceIdForReservation.collectAsState()
    val selectedClientId by calendarViewModel.selectedClientIdForReservation.collectAsState()
    val selectedDate by calendarViewModel.selectedDateForReservation.collectAsState()
    val selectedTime by calendarViewModel.selectedTimeForReservation.collectAsState()

    val services by serviceViewModel.localServicesList.collectAsState()
    val clients by clientViewModel.localClientsList.collectAsState()

    val selectedService = services.firstOrNull { it.id == selectedServiceId }
    val selectedClient = clients.firstOrNull { it.id == selectedClientId }

    // Navegación automática entre pasos
    LaunchedEffect(selectedServiceId) {
        if (selectedServiceId != null && currentStep == ReservationStep.SELECT_SERVICE) {
            delay(300)
            currentStep = ReservationStep.SELECT_DATETIME
        }
    }

    LaunchedEffect(selectedDate, selectedTime) {
        if (selectedDate != null && selectedTime != null && currentStep == ReservationStep.SELECT_DATETIME) {
            delay(300)
            currentStep = ReservationStep.SELECT_CLIENT
        }
    }

    LaunchedEffect(selectedClientId) {
        if (selectedClientId != null && currentStep == ReservationStep.SELECT_CLIENT) {
            delay(300)
            currentStep = ReservationStep.SUMMARY
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Contenido principal
        when (currentStep) {
            ReservationStep.SELECT_SERVICE -> {
                SelectServiceDialog(
                    onDismiss = {
                        calendarViewModel.clearReservationForm()
                        onDismiss()
                    },
                    onServiceSelected = { service ->
                        calendarViewModel.setSelectedServiceForReservation(service.id)
                    }
                )
            }

            ReservationStep.SELECT_DATETIME -> {
                SelectDateTimeDialog(
                    initialDate = selectedDate ?: LocalDate.now(),
                    initialTime = selectedTime,
                    onDismiss = {
                        currentStep = ReservationStep.SELECT_SERVICE
                        calendarViewModel.setSelectedDateForReservation(null)
                        calendarViewModel.setSelectedTimeForReservation(null)
                    },
                    onConfirm = { date, time ->
                        calendarViewModel.setSelectedDateForReservation(date)
                        calendarViewModel.setSelectedTimeForReservation(time)
                    }
                )
            }

            ReservationStep.SELECT_CLIENT -> {
                SelectClientDialog(
                    onDismiss = {
                        currentStep = ReservationStep.SELECT_DATETIME
                        calendarViewModel.setSelectedClientForReservation(null)
                    },
                    onClientSelected = { client ->
                        calendarViewModel.setSelectedClientForReservation(client.id)
                    }
                )
            }

            ReservationStep.SUMMARY -> {
                ReservationSummarySheet(
                    service = selectedService,
                    client = selectedClient,
                    date = selectedDate,
                    time = selectedTime,
                    onDismiss = {
                        calendarViewModel.clearReservationForm()
                        onDismiss()
                    },
                    onCreate = {
                        // TODO: Llamar a la API aquí
                        showSuccessMessage = true
                    }
                )
            }
        }

        // Mensaje de éxito
        AnimatedVisibility(
            visible = showSuccessMessage,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            SuccessMessage(
                onComplete = {
                    showSuccessMessage = false
                    calendarViewModel.clearReservationForm()
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun SuccessMessage(onComplete: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onComplete()
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1A1A1A)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Creado exitosamente",
                fontSize = 15.sp,
                fontWeight = FontWeight.W500,
                color = Color.White
            )
        }
    }
}
