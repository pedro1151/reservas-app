package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steofour

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.clients.ClientResponse
import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationStatus
import com.optic.pramosreservasappz.domain.model.reservations.ReservationType
import com.optic.pramosreservasappz.domain.model.services.ServiceResponse
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ServiceReservationStep
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import com.optic.pramosreservasappz.presentation.screens.services.ServiceViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


// ── Step 4: Resumen ────────────────────────────────────────────────────────────
@Composable
fun ResumeContent(
    navController: NavHostController,
    calendarViewModel: CalendarViewModel
) {

    val serviceViewModel: ServiceViewModel = hiltViewModel()
    val clientViewModel: ClientViewModel = hiltViewModel()

    val selectedServiceId by calendarViewModel.selectedServiceIdForReservation.collectAsState()
    val selectedClientId  by calendarViewModel.selectedClientIdForReservation.collectAsState()
    val selectedDate      by calendarViewModel.selectedDateForReservation.collectAsState()
    val selectedTime      by calendarViewModel.selectedTimeForReservation.collectAsState()

    val date = selectedDate
    val time = selectedTime

    val services          by serviceViewModel.localServicesList.collectAsState()
    val clients           by clientViewModel.localClientsList.collectAsState()

    val service = services.firstOrNull { it.id == selectedServiceId }
    val client  = clients.firstOrNull  { it.id == selectedClientId }

    val timeFmt = remember { DateTimeFormatter.ofPattern("HH:mm") }

    val createState by calendarViewModel.createReservationState.collectAsState()

    LaunchedEffect(createState) {
        if (createState is Resource.Success) {

            calendarViewModel.clearReservationForm()

            navController.navigate(ClientScreen.Calendario.route) {
                popUpTo(ClientScreen.Calendario.route) { inclusive = true }
            }
        }
    }


    Column(Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)) {
            if (service != null) item {
                SummaryCard {
                    SummaryRow(Icons.Outlined.MiscellaneousServices, Color(0xFF009688), "Servicio", service.name)
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(start = 44.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 44.dp, top = 12.dp, bottom = 12.dp), Arrangement.spacedBy(40.dp)) {
                        LabelValue("Costo", "Bs ${service.price ?: 0}")
                        LabelValue("Duración", "${service.durationMinutes} min")
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
            if (date != null && time != null) item {
                val dow = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es","ES")).replaceFirstChar { it.uppercase() }
                val end = time.plusMinutes(service?.durationMinutes?.toLong() ?: 0)
                SummaryCard {
                    SummaryRow(Icons.Outlined.CalendarToday, Color(0xFF9E9E9E), "Fecha",
                        "$dow ${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.SHORT, Locale("es","ES"))} ${date.year}")
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(start = 44.dp))
                    SummaryRow(Icons.Outlined.AccessTime, Color(0xFF9E9E9E), "Hora",
                        "${time.format(timeFmt)} – ${end.format(timeFmt)}")
                }
                Spacer(Modifier.height(12.dp))
            }
            if (client != null) item {
                SummaryCard {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp), Arrangement.spacedBy(14.dp), Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(44.dp)
                                .background(avatarColor(client.id), CircleShape), Alignment.Center) {
                            Text(initials(client.fullName), fontSize = 15.sp, fontWeight = FontWeight.W700, color = Color.White)
                        }
                        Column(Modifier.weight(1f)) {
                            Text(client.fullName, fontSize = 15.sp, fontWeight = FontWeight.W500, color = Color(0xFF111111))
                            client.email?.let { Text(it, fontSize = 12.sp, color = Color(0xFF9E9E9E)) }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
            item {
                SummaryCard {
                    SummaryRow(Icons.Outlined.Repeat, Color(0xFF9E9E9E), "Repetir", "No se repite")
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(start = 44.dp))
                    SummaryRow(Icons.Outlined.Description, Color(0xFF9E9E9E), "Notas internas",
                        "") // TODO: API — calendarViewModel.internalNoteForReservation.collectAsState()
                    HorizontalDivider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(start = 44.dp))
                    SummaryRow(Icons.Outlined.LocalOffer, Color(0xFF9E9E9E), "Etiqueta", "Sin etiqueta")
                }
            }
        }
        Surface(color = Color.White, shadowElevation = 6.dp) {
            Button(
                onClick = {
                    if (calendarViewModel.isReservationFormComplete()) {

                        // ── Construcción de fechas ─────────────────────────────
                        val startDateTime = selectedDate!!.atTime(selectedTime)

                        val endDateTime = startDateTime
                            .plusMinutes(service!!.durationMinutes.toLong())

                        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

                        val request = ReservationCreateRequest(
                            type = ReservationType.SERVICE,
                            providerId = 1,
                            startTime = startDateTime.format(formatter),
                            endTime = endDateTime.format(formatter),
                            status = ReservationStatus.PENDING,
                            staffId = null,
                            clientId = selectedClientId,
                            serviceId = selectedServiceId,
                            classmateId = null,
                            eventId = null,
                            meetingId = null,
                            internalNote = null,
                            createdBy = "PRUEBA"
                        )

                        calendarViewModel.createReservation(request)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF111111), contentColor = Color.White),
                shape    = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Crear cita", fontSize = 15.sp, fontWeight = FontWeight.W600, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

// ── Reusables ──────────────────────────────────────────────────────────────────
@Composable
private fun SummaryCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFF9F9F9), modifier = Modifier.fillMaxWidth()) {
        Column(content = content)
    }
}

@Composable
private fun SummaryRow(icon: ImageVector, iconTint: Color, label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp), Arrangement.spacedBy(12.dp), Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(20.dp), tint = iconTint)
        Column(Modifier.weight(1f)) {
            Text(label, fontSize = 12.sp, color = Color(0xFF9E9E9E), fontWeight = FontWeight.W400)
            if (value.isNotBlank()) Text(value, fontSize = 15.sp, color = Color(0xFF111111), fontWeight = FontWeight.W400)
        }
    }
}

@Composable
private fun LabelValue(label: String, value: String) {
    Column {
        Text(label, fontSize = 12.sp, color = Color(0xFF9E9E9E))
        Text(value, fontSize = 15.sp, color = Color(0xFF111111), fontWeight = FontWeight.W500)
    }
}

@Composable
private fun EmptyState(label: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 80.dp), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.SearchOff, null, tint = Color(0xFFDDDDDD), modifier = Modifier.size(40.dp))
            Text(label, fontSize = 13.sp, color = Color(0xFFBBBBBB))
        }
    }
}

@Composable
private fun SuccessSnackbar() {
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(14.dp), Color(0xFF111111), shadowElevation = 10.dp) {
        Row(Modifier.padding(horizontal = 20.dp, vertical = 16.dp), Arrangement.spacedBy(14.dp), Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(22.dp))
            Column {
                Text("Cita creada con éxito", fontSize = 15.sp, fontWeight = FontWeight.W600, color = Color.White)
                Text("Aparecerá en tu calendario", fontSize = 12.sp, color = Color(0xFF888888))
            }
        }
    }
}

// ── Helpers ────────────────────────────────────────────────────────────────────
private fun initials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}

private fun avatarColor(id: Int): Color {
    val palette = listOf(
        Color(0xFF1A1A1A), Color(0xFF4A6CF7), Color(0xFF7C3AED),
        Color(0xFF059669), Color(0xFFDC2626), Color(0xFFD97706),
        Color(0xFF0891B2), Color(0xFFDB2777), Color(0xFF555555)
    )
    return palette[id % palette.size]
}
