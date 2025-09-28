package com.optic.ecommerceappmvvm.presentation.screens.matches.datetopbar


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import android.os.Build
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import java.time.format.DateTimeFormatter



@Composable
fun MatchesDateTopBar(
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = remember { LocalDate.of(2023, 9, 17) } // HARDCODE: hoy
    val daysRange = remember { (-10..10).map { today.plusDays(it.toLong()) } }

    var selectedDate by remember { mutableStateOf(today) }
    val listState = rememberLazyListState()

    // Scroll animado hasta "hoy"
    LaunchedEffect(Unit) {
        listState.animateScrollToItem(10)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            itemsIndexed(daysRange) { _, date ->
                val isSelected = date == selectedDate
                val label = getDateLabel(date, today)

                Column(
                    modifier = Modifier
                        .clickable {
                            selectedDate = date
                            onDateSelected(date)
                        }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .height(3.dp)
                            .width(24.dp)
                            .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.tertiary
                                else Color.Transparent
                            )
                    )
                }
            }
        }
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    }
}


fun getDateLabel(date: LocalDate, today: LocalDate): String {
    val formatterDay = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
    val formatterMonth = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())

    return when {
        date == today.minusDays(1) -> "ayer"
        date == today -> "hoy"
        date == today.plusDays(1) -> "mañana"
        else -> "${date.format(formatterDay)} ${date.format(formatterMonth)}"
    }.replaceFirstChar { it.uppercaseChar() } // primera letra en mayúscula
}


@Composable
fun DateChip(
    label: String,
    subLabel: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(60.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = if (selected) 4.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = subLabel,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}