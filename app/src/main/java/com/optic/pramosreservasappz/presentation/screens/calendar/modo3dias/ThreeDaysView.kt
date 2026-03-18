package com.optic.pramosreservasappz.presentation.screens.calendar.modo3dias

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
import androidx.compose.ui.text.style.TextAlign
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
import java.time.format.TextStyle
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

// ── Palette ───────────────────────────────────────────────────────────────────
private val TDBlack   = Color(0xFF0D0D0D)
private val TDGray100 = Color(0xFFF2F2F2)
private val TDGray200 = Color(0xFFE8E8E8)
private val TDGray400 = Color(0xFFAAAAAA)
private val TDGray600 = Color(0xFF666666)
private val TDTodayBg = Color(0xFFF8F8F8)

private val tdPalette = listOf(
    Color(0xFF5C6BC0), Color(0xFF26A69A), Color(0xFF7E57C2),
    Color(0xFF26C6DA), Color(0xFFEF5350), Color(0xFFFF7043),
    Color(0xFF66BB6A), Color(0xFFEC407A), Color(0xFF78909C)
)
private fun tdAccent(id: Int?) = tdPalette[(id ?: 0) % tdPalette.size]

private val HOUR_H   = 80.dp
private val CHIP_H   = 72.dp
private val TIME_COL = 52.dp
private val SWIPE_PX = 80f

// ─────────────────────────────────────────────────────────────────────────────
//  ENTRY POINT
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ThreeDaysView(
    selectedDate: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    reservations: List<ReservationResponseComplete> = emptyList()
) {
    // currentDate: estado interno del pager
    var currentDate by remember { mutableStateOf(selectedDate) }
    LaunchedEffect(selectedDate) { currentDate = selectedDate }

    val scope       = rememberCoroutineScope()
    val offsetX     = remember { Animatable(0f) }
    var pageWidthPx by remember { mutableStateOf(1) }
    var dragTotal   by remember { mutableStateOf(0f) }
    var isSettling  by remember { mutableStateOf(false) }

    // Las 3 ventanas de 3 días cada una
    val prevStart = remember(currentDate) { currentDate.minusDays(3) }
    val nextStart = remember(currentDate) { currentDate.plusDays(3) }

    Column(Modifier.fillMaxSize().background(Color.White)) {

        // ── Header — muestra los días de la ventana actual ────────────────────
        ThreeDaysHeader(
            days = (0..2).map { currentDate.plusDays(it.toLong()) }
        )
        HorizontalDivider(color = TDGray200, thickness = 0.5.dp)

        // ── Pager de 3 paneles ────────────────────────────────────────────────
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
                                        dragTotal >  SWIPE_PX -> {       // retroceder 3 días
                                            offsetX.animateTo(w, tween(280, easing = FastOutSlowInEasing))
                                            currentDate = prevStart
                                            onDateSelect(prevStart)
                                            offsetX.snapTo(0f)
                                        }
                                        dragTotal < -SWIPE_PX -> {       // avanzar 3 días
                                            offsetX.animateTo(-w, tween(280, easing = FastOutSlowInEasing))
                                            currentDate = nextStart
                                            onDateSelect(nextStart)
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
                                offsetX.animateTo(0f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium))
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

            // Panel ANTERIOR
            Box(Modifier.fillMaxSize().offset { IntOffset((offsetX.value - w).roundToInt(), 0) }) {
                ThreeDaysTimeline(
                    startDate    = prevStart,
                    reservations = reservations
                )
            }

            // Panel ACTUAL
            Box(Modifier.fillMaxSize().offset { IntOffset(offsetX.value.roundToInt(), 0) }) {
                ThreeDaysTimeline(
                    startDate    = currentDate,
                    reservations = reservations
                )
            }

            // Panel SIGUIENTE
            Box(Modifier.fillMaxSize().offset { IntOffset((offsetX.value + w).roundToInt(), 0) }) {
                ThreeDaysTimeline(
                    startDate    = nextStart,
                    reservations = reservations
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  HEADER con los 3 días
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ThreeDaysHeader(days: List<LocalDate>) {
    val today = LocalDate.now()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = TIME_COL)
    ) {
        days.forEachIndexed { idx, date ->
            val isToday  = date == today
            val dayLabel = date.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale("es", "ES"))
                .replaceFirstChar { it.uppercase() }

            Column(
                modifier            = Modifier.weight(1f).padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    dayLabel,
                    fontSize      = 11.sp,
                    fontWeight    = FontWeight.W500,
                    color         = if (isToday) TDBlack else TDGray400,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(5.dp))
                // Cuadrado redondeado negro para hoy — igual que Setmore
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(if (isToday) TDBlack else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        date.dayOfMonth.toString(),
                        fontSize   = 15.sp,
                        fontWeight = if (isToday) FontWeight.W800 else FontWeight.W400,
                        color      = if (isToday) Color.White else TDGray600,
                        textAlign  = TextAlign.Center
                    )
                }
            }
            if (idx < days.lastIndex) {
                Box(
                    Modifier.width(0.5.dp).height(60.dp)
                        .background(TDGray200)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  TIMELINE INTERNA (3 columnas)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ThreeDaysTimeline(
    startDate: LocalDate,
    reservations: List<ReservationResponseComplete>
) {
    val days      = (0..2).map { startDate.plusDays(it.toLong()) }
    val hours     = (6..22).map { LocalTime.of(it, 0) }
    val listState = rememberLazyListState()

    val byDate = remember(reservations) {
        reservations.groupBy {
            LocalDateTime.parse(it.startTime, DateTimeFormatter.ISO_DATE_TIME).toLocalDate()
        }
    }

    LaunchedEffect(startDate) {
        val scrollTo = ((LocalTime.now().hour - 6) - 1).coerceAtLeast(0)
        listState.scrollToItem(scrollTo)
    }

    LazyColumn(
        state          = listState,
        modifier       = Modifier.fillMaxSize().background(Color.White),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(count = hours.size, key = { it }) { index ->
            ThreeDaysRow(hours[index], days, byDate)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  FILA DE HORA
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ThreeDaysRow(
    time: LocalTime,
    days: List<LocalDate>,
    byDate: Map<LocalDate, List<ReservationResponseComplete>>
) {
    val isCurrentHour = LocalTime.now().hour == time.hour
    val maxEvents = days.maxOf { d ->
        byDate[d]?.count {
            LocalDateTime.parse(it.startTime, DateTimeFormatter.ISO_DATE_TIME).hour == time.hour
        } ?: 0
    }
    val rowHeight = if (maxEvents <= 1) HOUR_H else HOUR_H + (CHIP_H + 4.dp) * (maxEvents - 1)

    val label = when (time.hour) {
        0 -> "12AM"; 12 -> "12PM"
        in 1..11 -> "${time.hour}AM"
        else     -> "${time.hour - 12}PM"
    }

    Row(Modifier.fillMaxWidth().height(rowHeight)) {

        // Etiqueta hora
        Box(
            Modifier.width(TIME_COL).fillMaxHeight().padding(end = 10.dp, top = 4.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                label,
                fontSize   = 11.sp,
                fontWeight = if (isCurrentHour) FontWeight.W700 else FontWeight.W400,
                color      = if (isCurrentHour) TDBlack else TDGray400
            )
        }

        // Columnas de días
        days.forEachIndexed { idx, date ->
            val events  = (byDate[date] ?: emptyList()).filter {
                LocalDateTime.parse(it.startTime, DateTimeFormatter.ISO_DATE_TIME).hour == time.hour
            }
            val isToday = date == LocalDate.now()

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (isToday) TDTodayBg else Color.White)
            ) {
                HorizontalDivider(Modifier.align(Alignment.TopCenter), color = TDGray100, thickness = 0.5.dp)

                if (events.isNotEmpty()) {
                    Column(
                        Modifier.fillMaxSize().padding(horizontal = 3.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) { events.forEach { CompactEventChip(it) } }
                }

                // Línea "ahora"
                if (isCurrentHour && isToday) {
                    val progress = LocalTime.now().minute / 60f
                    Row(
                        modifier          = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .offset(y = (progress * HOUR_H.value).dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(TDBlack))
                        HorizontalDivider(Modifier.weight(1f), color = TDBlack, thickness = 1.5.dp)
                    }
                }
            }

            if (idx < days.lastIndex) {
                Box(Modifier.width(0.5.dp).fillMaxHeight().background(TDGray200))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CHIP COMPACTO
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CompactEventChip(reservation: ReservationResponseComplete) {
    val timeFmt = DateTimeFormatter.ofPattern("HH:mm")
    val start   = runCatching {
        LocalDateTime.parse(reservation.startTime, DateTimeFormatter.ISO_DATE_TIME)
    }.getOrNull()

    val accent = try {
        val raw = reservation.service?.color
        if (raw.isNullOrBlank()) tdAccent(reservation.service?.id)
        else Color(android.graphics.Color.parseColor(raw))
    } catch (e: Exception) { tdAccent(reservation.service?.id) }

    val initial = reservation.client?.fullName?.trim()?.firstOrNull()?.uppercase() ?: "?"

    Box(
        Modifier.fillMaxWidth().height(CHIP_H)
            .clip(RoundedCornerShape(10.dp))
            .background(accent.copy(alpha = 0.11f))
    ) {
        Box(Modifier.width(3.dp).fillMaxHeight()
            .background(accent, RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)))

        Column(
            Modifier.fillMaxSize().padding(start = 8.dp, end = 5.dp, top = 7.dp, bottom = 7.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Box(Modifier.size(18.dp).clip(CircleShape).background(accent), Alignment.Center) {
                    Text(initial, fontSize = 8.sp, fontWeight = FontWeight.W800, color = Color.White)
                }
                Text(
                    reservation.client?.fullName ?: "Cliente",
                    fontSize = 11.sp, fontWeight = FontWeight.W700, color = TDBlack,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(reservation.service?.name ?: "", fontSize = 10.sp, fontWeight = FontWeight.W500, color = accent, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (start != null) {
                Text(start.toLocalTime().format(timeFmt), fontSize = 9.sp, fontWeight = FontWeight.W600, color = TDGray400)
            }
        }
    }
}
