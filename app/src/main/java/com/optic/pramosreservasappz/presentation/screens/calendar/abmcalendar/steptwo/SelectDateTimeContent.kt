package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.steptwo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.Navigation
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.calendar.CalendarViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


// ── Step 2: Fecha y hora ───────────────────────────────────────────────────────
@Composable
fun SelectDateTimeContent(
    initialDate: LocalDate,
    initialTime: LocalTime?,
    onConfirm: (LocalDate, LocalTime) -> Unit,
    navController: NavHostController,
    calendarViewModel : CalendarViewModel
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate by remember { mutableStateOf(initialDate) }
    var selectedTime by remember { mutableStateOf(initialTime) }
    val scroll = rememberScrollState()

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(scroll)) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                IconButton({ currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(Icons.Default.ChevronLeft, null, tint = Color(0xFF555555))
                }
                Text(
                    "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es","ES")).replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
                    fontSize = 15.sp, fontWeight = FontWeight.W600, color = Color(0xFF111111)
                )
                IconButton({ currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF555555))
                }
            }
            InlineCalendar(currentMonth, selectedDate) { selectedDate = it }
            Spacer(Modifier.height(20.dp))
            Text(
                "Hora", fontSize = 13.sp, fontWeight = FontWeight.W600,
                color = Color(0xFF9E9E9E), letterSpacing = 0.8.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
            InlineTimeGrid(selectedTime) { selectedTime = it }
            Spacer(Modifier.height(100.dp))
        }
        Surface(color = Color.White, shadowElevation = 6.dp) {
            Button(
                onClick  = {

                    if (selectedTime != null) {
                        calendarViewModel.selectDateTimeAndContinue(selectedDate, selectedTime!!)
                        navController.navigate(ClientScreen.CreateReservationStepThree.route)
                    }
                },
                enabled  = selectedTime != null,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = Color(0xFF111111),
                    disabledContainerColor = Color(0xFFE0E0E0),
                    contentColor           = Color.White,
                    disabledContentColor   = Color(0xFFAAAAAA)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    "Confirmar",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
private fun InlineCalendar(currentMonth: YearMonth, selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val today          = LocalDate.now()
    val firstDay       = currentMonth.atDay(1)
    val daysInMonth    = currentMonth.lengthOfMonth()
    val firstDayOfWeek = firstDay.dayOfWeek.value

    Column(Modifier.padding(horizontal = 20.dp)) {
        Row(Modifier.fillMaxWidth()) {
            listOf("L","M","M","J","V","S","D").forEach { label ->
                Text(label, Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp, fontWeight = FontWeight.W500, color = Color(0xFFBBBBBB))
            }
        }
        Spacer(Modifier.height(10.dp))
        val weeks = ((daysInMonth + firstDayOfWeek - 1) + 6) / 7
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            for (week in 0 until weeks) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (dow in 1..7) {
                        val dom = week * 7 + dow - firstDayOfWeek + 1
                        if (dom in 1..daysInMonth) {
                            val date    = currentMonth.atDay(dom)
                            val isSel   = date == selectedDate
                            val isToday = date == today
                            Box(
                                Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) Color(0xFF111111) else Color.Transparent)
                                    .clickable { onDateSelected(date) },
                                Alignment.Center
                            ) {
                                Text(dom.toString(), fontSize = 14.sp,
                                    fontWeight = if (isSel || isToday) FontWeight.W700 else FontWeight.W400,
                                    color = when { isSel -> Color.White; isToday -> Color(0xFF111111); else -> Color(0xFF555555) })
                                if (isToday && !isSel) {
                                    Box(Modifier.size(4.dp).background(Color(0xFF111111), CircleShape)
                                        .align(Alignment.BottomCenter).offset(y = (-3).dp))
                                }
                            }
                        } else Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun InlineTimeGrid(selectedTime: LocalTime?, onTimeSelected: (LocalTime) -> Unit) {
    val slots = remember { buildList { for (h in 7..21) for (m in listOf(0, 30)) add(LocalTime.of(h, m)) } }
    val fmt   = remember { DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH) }
    LazyVerticalGrid(
        columns               = GridCells.Fixed(4),
        modifier              = Modifier.fillMaxWidth().height(320.dp).padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement   = Arrangement.spacedBy(8.dp)
    ) {
        items(slots) { time ->
            val sel = time == selectedTime
            Box(
                Modifier.fillMaxWidth().height(44.dp).clip(RoundedCornerShape(10.dp))
                    .background(if (sel) Color(0xFF111111) else Color(0xFFF6F6F6))
                    .clickable { onTimeSelected(time) },
                Alignment.Center
            ) {
                Text(time.format(fmt), fontSize = 11.sp,
                    fontWeight = if (sel) FontWeight.W700 else FontWeight.W400,
                    color = if (sel) Color.White else Color(0xFF555555))
            }
        }
    }
}
