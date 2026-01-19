package com.optic.pramosfootballappz.presentation.screens.matches.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.optic.pramosfootballappz.presentation.ui.theme.getGreenColorFixture
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    selectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit,
    onCancel: () -> Unit
) {
    var currentSelected by remember { mutableStateOf(selectedDate) }
    val today = LocalDate.now()

    // --- MOSTRAR 2 MESES ANTES Y 2 DESPUÉS ---
    val monthList = remember {
        val center = YearMonth.from(today)
        (-2..2).map { center.plusMonths(it.toLong()) }
    }

    // --- SCROLLEAR AUTOMÁTICAMENTE AL MES ACTUAL ---
    val listState = rememberLazyListState()
    val currentMonthIndex = 2 // siempre en medio (-2..2)

    LaunchedEffect(Unit) {
        listState.scrollToItem(currentMonthIndex)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {

        // ------------ TOP BAR -------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { onCancel() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Cerrar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Hoy",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onDateSelected(today)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.getGreenColorFixture,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // ---------- SCROLL DE MESES ----------
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            items(monthList) { ym ->
                MonthView(
                    month = ym,
                    selectedDate = currentSelected,
                    onDaySelected = { date ->
                        currentSelected = date
                        onDateSelected(date)
                    }
                )
            }
        }
    }
}


@Composable
fun MonthView(
    month: YearMonth,
    selectedDate: LocalDate?,
    onDaySelected: (LocalDate) -> Unit
) {
    val daysInMonth = month.lengthOfMonth()
    val firstDay = month.atDay(1)
    val startOffset = (firstDay.dayOfWeek.value % 7)
    val days = (1..daysInMonth).map { month.atDay(it) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                .replaceFirstChar { it.uppercase() } + " " + month.year,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        val weekDays = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekDays.forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val totalCells = startOffset + days.size
        val rows = (totalCells / 7) + (if (totalCells % 7 != 0) 1 else 0)

        Column {
            var dayIndex = 0

            repeat(rows) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(7) { col ->
                        val cellIndex = row * 7 + col
                        if (cellIndex < startOffset || dayIndex >= days.size) {
                            Spacer(modifier = Modifier.size(40.dp))
                        } else {
                            val date = days[dayIndex]
                            DayItem(
                                date = date,
                                isSelected = date == selectedDate,
                                isToday = date == LocalDate.now(),
                                onClick = { onDaySelected(date) }
                            )
                            dayIndex++
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DayItem(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.getGreenColorFixture.copy(alpha = 0.15f)
                    else -> Color.Transparent
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                else -> MaterialTheme.colorScheme.onSurface
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
