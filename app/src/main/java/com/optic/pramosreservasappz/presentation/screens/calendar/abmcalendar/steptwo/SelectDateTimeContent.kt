package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptwo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

private val DTBlack   = Color(0xFF0D0D0D)
private val DTGray100 = Color(0xFFF2F2F2)
private val DTGray200 = Color(0xFFE8E8E8)
private val DTGray400 = Color(0xFFAAAAAA)
private val DTGray600 = Color(0xFF666666)

@Composable
fun SelectDateTimeContent(
    initialDate: LocalDate,
    initialTime: LocalTime?,
    onConfirm: (LocalDate, LocalTime) -> Unit,
    navController: NavHostController,
    calendarViewModel: CalendarViewModel
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate  by remember { mutableStateOf(initialDate) }
    var selectedTime  by remember { mutableStateOf(initialTime) }
    val scroll = rememberScrollState()

    Column(Modifier.fillMaxSize()) {

        Column(Modifier.weight(1f).verticalScroll(scroll)) {

            // ── Navegación de mes ─────────────────────────────────────────────
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick  = { currentMonth = currentMonth.minusMonths(1) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.ChevronLeft, null, tint = DTGray600, modifier = Modifier.size(22.dp))
                }
                Text(
                    text = "${
                        currentMonth.month
                            .getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                            .replaceFirstChar { it.uppercase() }
                    }  ${currentMonth.year}",
                    fontSize      = 16.sp,
                    fontWeight    = FontWeight.W600,
                    color         = DTBlack,
                    letterSpacing = (-0.3).sp
                )
                IconButton(
                    onClick  = { currentMonth = currentMonth.plusMonths(1) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.ChevronRight, null, tint = DTGray600, modifier = Modifier.size(22.dp))
                }
            }

            // ── Calendario ───────────────────────────────────────────────────
            InlineCalendar(currentMonth, selectedDate) { selectedDate = it }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = DTGray100)

            // ── Label "HORA"
            // FIX: padding(horizontal) y padding(top/bottom) en dos llamadas separadas
            Text(
                text          = "HORA",
                fontSize      = 11.sp,
                fontWeight    = FontWeight.W600,
                color         = DTGray400,
                letterSpacing = 1.sp,
                modifier      = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 10.dp)
            )

            // ── Grid de horas ─────────────────────────────────────────────────
            InlineTimeGrid(selectedTime) { selectedTime = it }

            Spacer(Modifier.height(100.dp))
        }

        // ── Botón Confirmar ───────────────────────────────────────────────────
        Surface(color = Color.White, shadowElevation = 4.dp) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                val canConfirm = selectedTime != null
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (canConfirm) DTBlack else DTGray200)
                        .clickable(
                            enabled           = canConfirm,
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null
                        ) {
                            if (selectedTime != null) {
                                calendarViewModel.selectDateTimeAndContinue(selectedDate, selectedTime!!)
                                navController.navigate(ClientScreen.CreateReservationStepThree.route)
                            }
                        }
                        .padding(vertical = 15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Confirmar",
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.W600,
                        color      = if (canConfirm) Color.White else DTGray400
                    )
                }
            }
        }
    }
}

@Composable
private fun InlineCalendar(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today          = LocalDate.now()
    val firstDay       = currentMonth.atDay(1)
    val daysInMonth    = currentMonth.lengthOfMonth()
    val firstDayOfWeek = firstDay.dayOfWeek.value

    Column(Modifier.padding(horizontal = 16.dp)) {

        Row(Modifier.fillMaxWidth()) {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { label ->
                Text(
                    label,
                    modifier   = Modifier.weight(1f),
                    textAlign  = TextAlign.Center,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.W500,
                    color      = DTGray400
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        val weeks = ((daysInMonth + firstDayOfWeek - 1) + 6) / 7

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (week in 0 until weeks) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (dow in 1..7) {
                        val dom = week * 7 + dow - firstDayOfWeek + 1
                        if (dom in 1..daysInMonth) {
                            val date    = currentMonth.atDay(dom)
                            val isSel   = date == selectedDate
                            val isToday = date == today
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(if (isSel) DTBlack else Color.Transparent)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication        = null
                                    ) { onDateSelected(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    dom.toString(),
                                    fontSize   = 14.sp,
                                    fontWeight = if (isSel || isToday) FontWeight.W700 else FontWeight.W400,
                                    color      = when {
                                        isSel   -> Color.White
                                        isToday -> DTBlack
                                        else    -> DTGray600
                                    },
                                    textAlign  = TextAlign.Center
                                )
                                if (isToday && !isSel) {
                                    Box(
                                        Modifier
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(DTBlack)
                                            .align(Alignment.BottomCenter)
                                            .offset(y = (-2).dp)
                                    )
                                }
                            }
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InlineTimeGrid(
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit
) {
    val slots = remember {
        buildList { for (h in 7..21) for (m in listOf(0, 30)) add(LocalTime.of(h, m)) }
    }

    LazyVerticalGrid(
        columns               = GridCells.Fixed(4),
        modifier              = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement   = Arrangement.spacedBy(8.dp)
    ) {
        items(slots) { time ->
            val sel    = time == selectedTime
            val hour   = if (time.hour == 0 || time.hour == 12) 12 else time.hour % 12
            val minute = String.format(Locale.US, "%02d", time.minute)
            val ampm   = if (time.hour < 12) "AM" else "PM"

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (sel) DTBlack else Color.White)
                    .then(
                        if (!sel) Modifier.border(1.dp, DTGray200, RoundedCornerShape(10.dp))
                        else Modifier
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null
                    ) { onTimeSelected(time) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment     = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text          = "$hour:$minute",
                        fontSize      = 12.sp,
                        fontWeight    = if (sel) FontWeight.W700 else FontWeight.W500,
                        color         = if (sel) Color.White else Color(0xFF333333),
                        letterSpacing = (-0.2).sp
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text     = ampm,
                        fontSize = 9.sp,
                        color    = if (sel) Color(0xAAFFFFFF) else DTGray400,
                        modifier = Modifier.padding(bottom = 1.5.dp)
                    )
                }
            }
        }
    }
}
