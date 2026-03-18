package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steofour

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.reservations.ReservationCreateRequest
import com.optic.pramosreservasappz.domain.model.reservations.ReservationStatus
import com.optic.pramosreservasappz.domain.model.reservations.ReservationType
import com.optic.pramosreservasappz.domain.util.Resource
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import com.optic.pramosreservasappz.presentation.screens.clients.ClientViewModel
import com.optic.pramosreservasappz.presentation.screens.services.ServiceViewModel
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlinx.coroutines.delay

// ── Palette ────────────────────────────────────────────────────────────────────
private val RCBlack   = Color(0xFF0D0D0D)
private val RCGray100 = Color(0xFFF2F2F2)
private val RCGray400 = Color(0xFFAAAAAA)
private val RCGray600 = Color(0xFF666666)

@Composable
fun ResumeContent(
    navController: NavHostController,
    calendarViewModel: CalendarViewModel
) {
    // ── lógica sin cambios ─────────────────────────────────────────────────────
    val serviceViewModel: ServiceViewModel = hiltViewModel()
    val clientViewModel: ClientViewModel   = hiltViewModel()

    val selectedServiceId by calendarViewModel.selectedServiceIdForReservation.collectAsState()
    val selectedClientId  by calendarViewModel.selectedClientIdForReservation.collectAsState()
    val selectedDate      by calendarViewModel.selectedDateForReservation.collectAsState()
    val selectedTime      by calendarViewModel.selectedTimeForReservation.collectAsState()

    val date = selectedDate
    val time = selectedTime

    val services by serviceViewModel.localServicesList.collectAsState()
    val clients  by clientViewModel.localClientsList.collectAsState()

    val service = services.firstOrNull { it.id == selectedServiceId }
    val client  = clients.firstOrNull  { it.id == selectedClientId  }

    val timeFmt = remember { DateTimeFormatter.ofPattern("HH:mm") }

    val createState by calendarViewModel.createReservationState.collectAsState()

    // ── Success overlay state ──────────────────────────────────────────────────
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(createState) {
        if (createState is Resource.Success) {
            showSuccess = true
            delay(2000)
            calendarViewModel.clearReservationForm()
            navController.navigate(ClientScreen.Calendario.route) {
                popUpTo(ClientScreen.Calendario.route) { inclusive = true }
            }
        }
    }

    // ── UI ─────────────────────────────────────────────────────────────────────
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier       = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                // ── Servicio ──────────────────────────────────────────────────
                if (service != null) item {
                    ResumeRow(Icons.Outlined.MiscellaneousServices, "Servicio", service.name)
                    Row(
                        modifier              = Modifier
                            .fillMaxWidth()
                            .padding(start = 56.dp, end = 20.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(40.dp)
                    ) {
                        SubLabelValue("Costo", "Bs ${service.price ?: 0}")
                        SubLabelValue("Duración", "${service.durationMinutes} min")
                    }
                    HorizontalDivider(color = RCGray100)
                }

                // ── Fecha y hora ──────────────────────────────────────────────
                if (date != null && time != null) item {
                    val dow = date.dayOfWeek
                        .getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                        .replaceFirstChar { it.uppercase() }
                    val mon = date.month
                        .getDisplayName(TextStyle.SHORT, Locale("es", "ES"))
                        .replaceFirstChar { it.uppercase() }
                    val end = time.plusMinutes(service?.durationMinutes?.toLong() ?: 0)

                    ResumeRow(Icons.Outlined.CalendarToday, "Fecha", "$dow ${date.dayOfMonth} $mon ${date.year}")
                    HorizontalDivider(color = RCGray100)
                    ResumeRow(Icons.Outlined.AccessTime, "Hora", "${time.format(timeFmt)} – ${end.format(timeFmt)}")
                    HorizontalDivider(color = RCGray100)
                }

                // ── Cliente ───────────────────────────────────────────────────
                if (client != null) item {
                    Row(
                        modifier              = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(40.dp).clip(CircleShape).background(resumeAvatarColor(client.id)),
                            Alignment.Center
                        ) {
                            Text(resumeInitials(client.fullName), fontSize = 13.sp, fontWeight = FontWeight.W700, color = Color.White)
                        }
                        Column(Modifier.weight(1f)) {
                            Text(client.fullName, fontSize = 15.sp, fontWeight = FontWeight.W500, color = RCBlack)
                            client.email?.let { Text(it, fontSize = 12.sp, color = RCGray400) }
                        }
                    }
                    HorizontalDivider(color = RCGray100)
                }

                // ── Extras ────────────────────────────────────────────────────
                item {
                    ResumeRow(Icons.Outlined.Repeat,      "Repetir",        "No se repite")
                    HorizontalDivider(color = RCGray100)
                    ResumeRow(Icons.Outlined.Description, "Notas internas", "")
                    HorizontalDivider(color = RCGray100)
                    ResumeRow(Icons.Outlined.LocalOffer,  "Etiqueta",       "Sin etiqueta")
                }
            }

            // ── Botón Crear cita ──────────────────────────────────────────────
            Surface(color = Color.White, shadowElevation = 4.dp) {
                Button(
                    onClick = {
                        if (calendarViewModel.isReservationFormComplete()) {
                            val startDateTime = selectedDate!!.atTime(selectedTime)
                            val endDateTime   = startDateTime.plusMinutes(service!!.durationMinutes.toLong())
                            val formatter     = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            calendarViewModel.createReservation(
                                ReservationCreateRequest(
                                    type         = ReservationType.SERVICE,
                                    providerId   = 1,
                                    startTime    = startDateTime.format(formatter),
                                    endTime      = endDateTime.format(formatter),
                                    status       = ReservationStatus.PENDING,
                                    staffId      = null,
                                    clientId     = selectedClientId,
                                    serviceId    = selectedServiceId,
                                    classmateId  = null,
                                    eventId      = null,
                                    meetingId    = null,
                                    internalNote = null,
                                    createdBy    = "PRUEBA"
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RCBlack, contentColor = Color.White),
                    shape  = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Crear cita", fontSize = 15.sp, fontWeight = FontWeight.W600)
                }
            }
        }

        // ── Overlay de éxito ──────────────────────────────────────────────────
        AnimatedVisibility(
            visible  = showSuccess,
            modifier = Modifier.align(Alignment.Center),
            enter    = scaleIn(
                initialScale  = 0.85f,
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
            ) + fadeIn(tween(200)),
            exit     = scaleOut(targetScale = 0.9f, animationSpec = tween(200)) + fadeOut(tween(200))
        ) {
            SuccessOverlay()
        }

        // Fondo oscuro detrás del overlay
        AnimatedVisibility(
            visible  = showSuccess,
            modifier = Modifier.matchParentSize(),
            enter    = fadeIn(tween(200)),
            exit     = fadeOut(tween(200))
        ) {
            Box(
                Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            )
        }
    }
}

// ── Success overlay card ───────────────────────────────────────────────────────
@Composable
private fun SuccessOverlay() {
    Box(
        modifier         = Modifier
            .padding(horizontal = 40.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Círculo verde con check
            Box(
                modifier         = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50).copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint     = Color(0xFF4CAF50),
                    modifier = Modifier.size(36.dp)
                )
            }
            Text(
                "¡Cita creada!",
                fontSize      = 18.sp,
                fontWeight    = FontWeight.W700,
                color         = RCBlack,
                letterSpacing = (-0.3).sp
            )
            Text(
                "Aparecerá en tu calendario",
                fontSize = 13.sp,
                color    = RCGray400
            )
        }
    }
}

// ── Fila plana ────────────────────────────────────────────────────────────────
@Composable
private fun ResumeRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment     = Alignment.Top
    ) {
        Icon(icon, null, tint = RCGray400, modifier = Modifier.size(20.dp).offset(y = 2.dp))
        Column(Modifier.weight(1f)) {
            Text(label, fontSize = 11.sp, color = RCGray400)
            if (value.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(value, fontSize = 14.sp, color = RCBlack)
            }
        }
    }
}

@Composable
private fun SubLabelValue(label: String, value: String) {
    Column {
        Text(label, fontSize = 11.sp, color = RCGray400)
        Text(value, fontSize = 14.sp, color = RCBlack, fontWeight = FontWeight.W500)
    }
}

// ── Helpers ────────────────────────────────────────────────────────────────────
private fun resumeInitials(fullName: String): String {
    val parts = fullName.trim().split(" ")
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(2).uppercase()
        else            -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
    }
}

private fun resumeAvatarColor(id: Int): Color {
    val palette = listOf(
        Color(0xFF1A1A1A), Color(0xFF4A6CF7), Color(0xFF7C3AED),
        Color(0xFF059669), Color(0xFFDC2626), Color(0xFFD97706),
        Color(0xFF0891B2), Color(0xFFDB2777), Color(0xFF555555)
    )
    return palette[id % palette.size]
}
