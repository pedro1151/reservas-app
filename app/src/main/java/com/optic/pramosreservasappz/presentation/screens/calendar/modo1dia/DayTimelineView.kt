package com.optic.pramosreservasappz.presentation.screens.calendar.modo1dia

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.domain.model.reservations.completeresponse.ReservationResponseComplete
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToInt

// ── Palette ───────────────────────────────────────────────────────────────────
private val DVBlack   = Color(0xFF0D0D0D)
private val DVGray100 = Color(0xFFF2F2F2)
private val DVGray400 = Color(0xFFAAAAAA)
private val DVGray600 = Color(0xFF666666)

private val dvPalette = listOf(
    Color(0xFF5C6BC0), Color(0xFF26A69A), Color(0xFF7E57C2),
    Color(0xFF26C6DA), Color(0xFFEF5350), Color(0xFFFF7043),
    Color(0xFF66BB6A), Color(0xFFEC407A), Color(0xFF78909C)
)
private fun dvAccent(id: Int?) = dvPalette[(id ?: 0) % dvPalette.size]

fun parseColor(color: String?): Color = try {
    if (color.isNullOrBlank()) Color(0xFF5C6BC0)
    else Color(android.graphics.Color.parseColor(color))
} catch (e: Exception) { Color(0xFF5C6BC0) }

private val HOUR_H      = 72.dp
private val TIME_W      = 56.dp
private val SWIPE_PX    = 80f

// ─────────────────────────────────────────────────────────────────────────────
//  ENTRY POINT
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun DayTimelineView(
    selectedDate: LocalDate,
    reservations: List<ReservationResponseComplete> = emptyList(),
    onTimeSlotClick: (LocalTime) -> Unit,
    onDateChange: (LocalDate) -> Unit = {}
) {
    // currentDate es el estado interno que controla qué "página central" se muestra.
    // Se sincroniza con selectedDate cuando cambia desde afuera.
    var currentDate by remember { mutableStateOf(selectedDate) }
    LaunchedEffect(selectedDate) { currentDate = selectedDate }

    val scope       = rememberCoroutineScope()
    val offsetX     = remember { Animatable(0f) }
    var pageWidthPx by remember { mutableStateOf(1) }
    var dragTotal   by remember { mutableStateOf(0f) }
    var isSettling  by remember { mutableStateOf(false) }

    // Las 3 páginas: prev · current · next
    val prevDate = remember(currentDate) { currentDate.minusDays(1) }
    val nextDate = remember(currentDate) { currentDate.plusDays(1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .onSizeChanged { pageWidthPx = it.width.coerceAtLeast(1) }
            .pointerInput(currentDate) {
                detectHorizontalDragGestures(
                    onDragStart  = { dragTotal = 0f },
                    onDragEnd    = {
                        if (!isSettling) {
                            scope.launch {
                                isSettling = true
                                val w = pageWidthPx.toFloat()
                                when {
                                    dragTotal >  SWIPE_PX -> {          // → ir a ANTERIOR
                                        offsetX.animateTo(
                                            w, tween(280, easing = FastOutSlowInEasing)
                                        )
                                        currentDate = prevDate
                                        onDateChange(prevDate)
                                        offsetX.snapTo(0f)
                                    }
                                    dragTotal < -SWIPE_PX -> {          // → ir a SIGUIENTE
                                        offsetX.animateTo(
                                            -w, tween(280, easing = FastOutSlowInEasing)
                                        )
                                        currentDate = nextDate
                                        onDateChange(nextDate)
                                        offsetX.snapTo(0f)
                                    }
                                    else -> offsetX.animateTo(
                                        0f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
                                    )
                                }
                                isSettling = false
                            }
                        }
                    },
                    onDragCancel = {
                        scope.launch {
                            offsetX.animateTo(
                                0f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
                            )
                        }
                    },
                    onHorizontalDrag = { _, delta ->
                        if (!isSettling) {
                            dragTotal += delta
                            scope.launch { offsetX.snapTo(offsetX.value + delta) }
                        }
                    }
                )
            }
    ) {
        val w = pageWidthPx.toFloat()

        // Página ANTERIOR — siempre a la izquierda (-pageWidth)
        Box(
            Modifier
                .fillMaxSize()
                .offset { IntOffset((offsetX.value - w).roundToInt(), 0) }
        ) {
            DayPage(
                date            = prevDate,
                reservations    = reservations,
                onTimeSlotClick = onTimeSlotClick
            )
        }

        // Página ACTUAL — centrada
        Box(
            Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        ) {
            DayPage(
                date            = currentDate,
                reservations    = reservations,
                onTimeSlotClick = onTimeSlotClick
            )
        }

        // Página SIGUIENTE — siempre a la derecha (+pageWidth)
        Box(
            Modifier
                .fillMaxSize()
                .offset { IntOffset((offsetX.value + w).roundToInt(), 0) }
        ) {
            DayPage(
                date            = nextDate,
                reservations    = reservations,
                onTimeSlotClick = onTimeSlotClick
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  UNA PÁGINA COMPLETA (un día)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun DayPage(
    date: LocalDate,
    reservations: List<ReservationResponseComplete>,
    onTimeSlotClick: (LocalTime) -> Unit
) {
    val hours     = (6..22).map { LocalTime.of(it, 0) }
    val listState = rememberLazyListState()

    val dayReservations = remember(reservations, date) {
        reservations.filter {
            runCatching {
                LocalDateTime.parse(it.startTime, DateTimeFormatter.ISO_DATE_TIME)
                    .toLocalDate() == date
            }.getOrDefault(false)
        }.sortedBy { it.startTime }
    }

    LaunchedEffect(date) {
        val scrollTo = ((LocalTime.now().hour - 6) - 1).coerceAtLeast(0)
        listState.scrollToItem(scrollTo)
    }

    LazyColumn(
        state          = listState,
        modifier       = Modifier.fillMaxSize().background(Color.White),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(count = hours.size, key = { it }) { i ->
            val hour      = hours[i]
            val isCurrent = date == LocalDate.now() && LocalTime.now().hour == hour.hour
            val events    = dayReservations.filter {
                LocalDateTime.parse(it.startTime, DateTimeFormatter.ISO_DATE_TIME).hour == hour.hour
            }
            DayHourSlot(
                time          = hour,
                isCurrentHour = isCurrent,
                reservations  = events,
                onSlotClick   = { onTimeSlotClick(hour) }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  SLOT DE HORA
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun DayHourSlot(
    time: LocalTime,
    isCurrentHour: Boolean,
    reservations: List<ReservationResponseComplete>,
    onSlotClick: () -> Unit
) {
    val slotHeight = if (reservations.isEmpty()) HOUR_H
    else HOUR_H * reservations.size

    val label = when (time.hour) {
        0        -> "12AM"; 12 -> "12PM"
        in 1..11 -> "${time.hour}AM"
        else     -> "${time.hour - 12}PM"
    }

    Box(modifier = Modifier.fillMaxWidth().height(slotHeight)) {

        HorizontalDivider(
            modifier  = Modifier.align(Alignment.TopStart).padding(start = TIME_W),
            color     = DVGray100,
            thickness = 0.8.dp
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            // Hora
            Box(
                Modifier.width(TIME_W).padding(end = 12.dp, top = 4.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    label,
                    fontSize   = 11.sp,
                    fontWeight = if (isCurrentHour) FontWeight.W700 else FontWeight.W400,
                    color      = if (isCurrentHour) DVBlack else DVGray400
                )
            }
            // Eventos
            Column(
                modifier            = Modifier.weight(1f).padding(end = 16.dp, top = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) { reservations.forEach { DayEventCard(it) } }
        }

        // Línea "ahora" negra — pill + punto + línea
        if (isCurrentHour) {
            val progress = LocalTime.now().minute / 60f
            val yOff     = (progress * HOUR_H.value).dp

            // Pill con hora
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 2.dp, y = yOff - 11.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(DVBlack)
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text(
                    LocalTime.now().format(DateTimeFormatter.ofPattern("H:mm")),
                    fontSize   = 10.sp,
                    fontWeight = FontWeight.W700,
                    color      = Color.White
                )
            }
            // Punto + línea
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .offset(y = yOff)
                    .padding(start = TIME_W - 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(10.dp).clip(CircleShape).background(DVBlack))
                HorizontalDivider(Modifier.weight(1f), color = DVBlack, thickness = 1.5.dp)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CARD DE EVENTO
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun DayEventCard(reservation: ReservationResponseComplete) {
    val fmt     = DateTimeFormatter.ISO_DATE_TIME
    val timeFmt = DateTimeFormatter.ofPattern("HH:mm")
    val start   = runCatching { LocalDateTime.parse(reservation.startTime, fmt) }.getOrNull()
    val end     = runCatching { LocalDateTime.parse(reservation.endTime,   fmt) }.getOrNull()

    val accent = try {
        val raw = reservation.service?.color
        if (raw.isNullOrBlank()) dvAccent(reservation.service?.id)
        else Color(android.graphics.Color.parseColor(raw))
    } catch (e: Exception) { dvAccent(reservation.service?.id) }

    val initials = reservation.client?.fullName?.trim()?.split(" ")?.let { p ->
        if (p.size == 1) p[0].take(2).uppercase()
        else "${p.first().firstOrNull()?.uppercase()}${p.last().firstOrNull()?.uppercase()}"
    } ?: "?"

    Box(
        Modifier.fillMaxWidth().height(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(accent.copy(alpha = 0.10f))
    ) {
        Box(Modifier.width(3.dp).fillMaxHeight()
            .background(accent, RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)))

        Row(
            Modifier.fillMaxSize().padding(start = 10.dp, end = 12.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(Modifier.size(34.dp).clip(CircleShape).background(accent), Alignment.Center) {
                Text(initials, fontSize = 11.sp, fontWeight = FontWeight.W800, color = Color.White)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    reservation.client?.fullName ?: "Cliente",
                    fontWeight = FontWeight.W600, fontSize = 13.sp, color = DVBlack,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(
                    reservation.service?.name ?: "Servicio",
                    fontSize = 11.sp, color = DVGray600,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                if (start != null && end != null) {
                    Text(
                        "${start.toLocalTime().format(timeFmt)}–${end.toLocalTime().format(timeFmt)}",
                        fontSize = 10.sp, fontWeight = FontWeight.W600, color = accent
                    )
                }
                reservation.service?.price?.let { p ->
                    if (p > 0) Text("Bs ${"%.0f".format(p)}", fontSize = 11.sp, fontWeight = FontWeight.W700, color = DVBlack)
                }
            }
        }
    }
}
