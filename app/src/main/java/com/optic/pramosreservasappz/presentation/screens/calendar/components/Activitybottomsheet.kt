package com.optic.pramosreservasappz.presentation.screens.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

data class ActivityItem(
    val id: Int,
    val actorInitial: String,
    val actorName: String,
    val action: String,          // texto en negrita
    val target: String,          // texto normal después
    val timestamp: LocalDateTime
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityBottomSheet(
    activities: List<ActivityItem>,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = "Actividad",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 17.sp
                )
            }

            HorizontalDivider(color = Color(0xFFF0F0F0))

            if (activities.isEmpty()) {
                // Estado vacío
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Sin actividad reciente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            } else {
                // Agrupar por fecha
                val grouped = activities.groupBy { it.timestamp.toLocalDate() }
                    .toSortedMap(compareByDescending { it })

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    grouped.forEach { (date, items) ->
                        item {
                            ActivityDateHeader(date = date)
                        }
                        items(items) { activity ->
                            ActivityRow(activity = activity)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityDateHeader(date: LocalDate) {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    val label = when (date) {
        today -> {
            val dow = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "ES"))
                .replaceFirstChar { it.uppercase() }
            val day = date.dayOfMonth
            val month = date.month.getDisplayName(TextStyle.SHORT, Locale("es", "ES"))
            val year = date.year
            "Hoy · $dow $day $month $year"
        }
        yesterday -> {
            val dow = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "ES"))
                .replaceFirstChar { it.uppercase() }
            "Ayer · $dow ${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.SHORT, Locale("es","ES"))} ${date.year}"
        }
        else -> {
            val dow = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                .replaceFirstChar { it.uppercase() }
            "$dow, ${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.FULL, Locale("es","ES"))} ${date.year}"
        }
    }

    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 13.sp,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
    )
}

@Composable
private fun ActivityRow(activity: ActivityItem) {
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mma", Locale("es", "ES"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Timestamp
        Text(
            text = activity.timestamp.format(timeFormatter).lowercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF9E9E9E),
            fontSize = 11.sp,
            modifier = Modifier
                .width(60.dp)
                .padding(top = 2.dp)
        )

        // Avatar inicial
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(Color(0xFFE0E0E0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = activity.actorInitial,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF424242),
                fontSize = 11.sp
            )
        }

        // Nombre + acción
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.actorName,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF757575),
                fontSize = 12.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = Color.Black)) {
                        append(activity.action)
                    }
                    append(" ")
                    withStyle(SpanStyle(color = Color(0xFF424242))) {
                        append("para ${activity.target}")
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}
