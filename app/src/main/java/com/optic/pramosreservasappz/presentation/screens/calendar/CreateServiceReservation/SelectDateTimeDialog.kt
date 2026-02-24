package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDateTimeDialog(
    initialDate: LocalDate,
    initialTime: LocalTime?,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate, LocalTime) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate by remember { mutableStateOf(initialDate) }
    var selectedTime by remember { mutableStateOf(initialTime) }

    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null,
        modifier = Modifier.fillMaxHeight(0.96f)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Seleccionar fecha y hora",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W500,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 17.sp
                )
            }

            HorizontalDivider(color = Color(0xFFF0F0F0))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                // Selector de mes
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { /* TODO: Abrir selector de mes/año */ }
                        )
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                            .replaceFirstChar { it.uppercase() },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Black
                    )
                    Text(
                        text = currentMonth.year.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = Color(0xFF757575)
                    )
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xFF9E9E9E)
                    )
                }

                // Calendario
                MiniCalendar(
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )

                Spacer(Modifier.height(24.dp))

                // Grid de horas
                TimeGrid(
                    selectedTime = selectedTime,
                    onTimeSelected = { selectedTime = it }
                )

                Spacer(Modifier.height(80.dp))
            }

            // Botón confirmar (fijo abajo)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        if (selectedTime != null) {
                            onConfirm(selectedDate, selectedTime!!)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D9488),
                        disabledContainerColor = Color(0xFFE0E0E0)
                    ),
                    enabled = selectedTime != null,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        "Confirmar",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniCalendar(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
    val today = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // Headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFF9E9E9E)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Days grid
        val weeks = (daysInMonth + firstDayOfWeek - 1) / 7 +
                if ((daysInMonth + firstDayOfWeek - 1) % 7 > 0) 1 else 0

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            for (week in 0 until weeks) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (dayOfWeek in 1..7) {
                        val dayOfMonth = week * 7 + dayOfWeek - firstDayOfWeek + 1

                        if (dayOfMonth in 1..daysInMonth) {
                            val date = currentMonth.atDay(dayOfMonth)
                            val isSelected = date == selectedDate
                            val isToday = date == today

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clickable(onClick = { onDateSelected(date) })
                                    .background(
                                        color = if (isSelected) Color.Black else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayOfMonth.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = when {
                                        isSelected -> FontWeight.W600
                                        isToday -> FontWeight.W700
                                        else -> FontWeight.W400
                                    },
                                    color = when {
                                        isSelected -> Color.White
                                        isToday -> Color.Black
                                        else -> Color(0xFF424242)
                                    }
                                )
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
private fun TimeGrid(
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit
) {
    // Generar slots de tiempo cada 15 minutos
    val timeSlots = remember {
        buildList {
            for (hour in 0..23) {
                for (minute in listOf(0, 15, 30, 45)) {
                    add(LocalTime.of(hour, minute))
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.height(600.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(timeSlots) { time ->
                TimeSlotButton(
                    time = time,
                    isSelected = time == selectedTime,
                    onClick = { onTimeSelected(time) }
                )
            }
        }
    }
}

@Composable
private fun TimeSlotButton(
    time: LocalTime,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val formatter = remember { java.time.format.DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH) }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Black else Color.White,
            contentColor = if (isSelected) Color.White else Color(0xFF757575)
        ),
        shape = RoundedCornerShape(8.dp),
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(
            1.dp,
            Color(0xFFE0E0E0)
        ) else null,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = time.format(formatter).uppercase(),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W400
        )
    }
}
