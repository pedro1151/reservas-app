package com.optic.pramosreservasappz.presentation.screens.calendar.modoagenda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.MiscellaneousServices
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.domain.model.reservations.completeresponse.ReservationResponseComplete
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

// ── Paleta ────────────────────────────────────────────────────────────────────
private val agendaPalette = listOf(
    Color(0xFF5C6BC0), Color(0xFF26A69A), Color(0xFF7E57C2),
    Color(0xFF26C6DA), Color(0xFFEF5350), Color(0xFFFF7043),
    Color(0xFF66BB6A), Color(0xFFEC407A), Color(0xFF78909C)
)
private fun accentFor(id: Int?) = agendaPalette[(id ?: 0) % agendaPalette.size]
private fun initials(name: String): String {
    val p = name.trim().split(" ")
    return if (p.size == 1) p[0].take(2).uppercase()
    else "${p.first().firstOrNull()?.uppercase() ?: ""}${p.last().firstOrNull()?.uppercase() ?: ""}"
}

@Composable
fun WeekAgendaView(
    selectedDate: LocalDate,
    reservations: List<ReservationResponseComplete>,
    onDateSelect: (LocalDate) -> Unit
) {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val days      = (0..6).map { selectedDate.plusDays(it.toLong()) }

    val reservationsByDate = reservations.groupBy {
        try { LocalDateTime.parse(it.startTime, formatter).toLocalDate() } catch (e: Exception) { null }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize().background(Color(0xFFF4F5F9)),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(days) { date ->
                val dayRes = (reservationsByDate[date] ?: emptyList())
                    .sortedBy { it.startTime }
                AgendaDaySection(date, dayRes, date == LocalDate.now())
            }
        }

        WeeklyEarningsBar(
            earnings = reservations.sumOf { it.service?.price ?: 0.0 },
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
        )
    }
}

// ── Sección de día ────────────────────────────────────────────────────────────
@Composable
private fun AgendaDaySection(
    date: LocalDate,
    reservations: List<ReservationResponseComplete>,
    isToday: Boolean
) {
    val dow   = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
    val month = date.month.getDisplayName(TextStyle.SHORT, Locale("es", "ES"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 20.dp, bottom = 4.dp)
    ) {
        // Cabecera del día
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Número destacado
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isToday) Color(0xFF5C6BC0) else Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    date.dayOfMonth.toString(),
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.W800,
                    color      = if (isToday) Color.White else Color(0xFF666666)
                )
            }

            Column {
                Text(
                    dow.replaceFirstChar { it.uppercase() },
                    fontSize   = 14.sp,
                    fontWeight = if (isToday) FontWeight.W700 else FontWeight.W500,
                    color      = if (isToday) Color(0xFF5C6BC0) else Color(0xFF444444)
                )
                Text(
                    "${month.replaceFirstChar { it.uppercase() }} ${date.year}",
                    fontSize = 11.sp,
                    color    = Color(0xFFAAAAAA)
                )
            }

            Spacer(Modifier.weight(1f))

            if (reservations.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isToday) Color(0xFF5C6BC0) else Color(0xFFEAEAEA))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        "${reservations.size} cita${if (reservations.size != 1) "s" else ""}",
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.W700,
                        color      = if (isToday) Color.White else Color(0xFF999999)
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        if (reservations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.6f))
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Text("Nada planeado", fontSize = 13.sp, color = Color(0xFFD0D0D0))
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                reservations.forEach { AgendaCard(it) }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
//  CARD AGENDA — horizontal moderna con gradiente lateral
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun AgendaCard(reservation: ReservationResponseComplete) {
    val fmt     = remember { DateTimeFormatter.ISO_DATE_TIME }
    val timeFmt = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val start   = runCatching { LocalDateTime.parse(reservation.startTime, fmt) }.getOrNull()
    val end     = runCatching { LocalDateTime.parse(reservation.endTime, fmt) }.getOrNull()

    val accent = try {
        val raw = reservation.service?.color
        if (raw.isNullOrBlank()) accentFor(reservation.service?.id)
        else Color(android.graphics.Color.parseColor(raw))
    } catch (e: Exception) { accentFor(reservation.service?.id) }

    val avatarBg  = accentFor(reservation.client?.id ?: 0)
    val clientIni = reservation.client?.fullName?.let { initials(it) } ?: "?"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
    ) {
        // Gradiente sutil de fondo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    Brush.horizontalGradient(
                        0f to accent.copy(alpha = 0.10f),
                        0.4f to Color.Transparent
                    )
                )
        )

        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra lateral de color
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(82.dp)
                    .background(accent, RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp))
            )

            // Bloque hora
            Column(
                modifier              = Modifier
                    .width(64.dp)
                    .padding(vertical = 14.dp),
                horizontalAlignment   = Alignment.CenterHorizontally,
                verticalArrangement   = Arrangement.Center
            ) {
                Text(
                    start?.toLocalTime()?.format(timeFmt) ?: "--",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.W800,
                    color      = Color(0xFF222222)
                )
                Text(
                    end?.toLocalTime()?.format(timeFmt) ?: "--",
                    fontSize = 11.sp,
                    color    = Color(0xFFCCCCCC)
                )
            }

            // Línea divisoria
            Box(
                modifier = Modifier
                    .width(0.8.dp)
                    .height(46.dp)
                    .background(Color(0xFFF0F0F0))
            )

            // Contenido principal
            Row(
                modifier              = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier         = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(avatarBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        clientIni,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.W800,
                        color      = Color.White
                    )
                }

                Column(
                    modifier            = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        reservation.client?.fullName ?: "Cliente",
                        fontWeight = FontWeight.W700,
                        fontSize   = 15.sp,
                        color      = Color(0xFF111111),
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Outlined.MiscellaneousServices, null,
                            modifier = Modifier.size(11.dp), tint = accent
                        )
                        Text(
                            reservation.service?.name ?: "Servicio",
                            fontSize = 12.sp, color = Color(0xFF888888),
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }
                    reservation.service?.durationMinutes?.let { dur ->
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                Icons.Outlined.AccessTime, null,
                                modifier = Modifier.size(10.dp), tint = Color(0xFFDDDDDD)
                            )
                            Text("$dur min", fontSize = 11.sp, color = Color(0xFFCCCCCC))
                        }
                    }
                }
            }

            // Precio badge
            reservation.service?.price?.let { price ->
                if (price > 0) {
                    Box(
                        modifier = Modifier
                            .padding(end = 14.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(accent.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "Bs ${"%,.0f".format(price)}",
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.W800,
                            color      = accent
                        )
                    }
                }
            }
        }
    }
}

// ── Barra de ingresos ─────────────────────────────────────────────────────────
@Composable
private fun WeeklyEarningsBar(earnings: Double, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Color.White, shadowElevation = 12.dp) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF0F1FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.CreditCard, null,
                        modifier = Modifier.size(17.dp), tint = Color(0xFF5C6BC0)
                    )
                }
                Text(
                    "Ingresos esta semana",
                    color    = Color(0xFF666666),
                    fontSize = 13.sp
                )
            }
            Text(
                "Bs ${"%.2f".format(earnings)}",
                fontWeight = FontWeight.W800,
                color      = Color(0xFF111111),
                fontSize   = 16.sp
            )
        }
    }
}
