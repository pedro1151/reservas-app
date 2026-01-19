package com.optic.ecommerceappmvvm.presentation.screens.matches.datetopbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.util.Locale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import java.time.format.DateTimeFormatter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import com.optic.ecommerceappmvvm.R
import com.optic.ecommerceappmvvm.presentation.settings.idiomas.LocalizedContext
import kotlinx.coroutines.launch

@Composable
fun MatchesDateTopBar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = remember { LocalDate.now() }
    val daysRange = remember { (-60..60).map { today.plusDays(it.toLong()) } }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val screenWidthPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val itemWidthPx = 120f
    val centerOffset = (screenWidthPx / 2 - itemWidthPx / 2).toInt()

    // 🔥 Cada vez que selectedDate cambie → centrar esa fecha
    LaunchedEffect(selectedDate) {
        val index = daysRange.indexOf(selectedDate)
        if (index >= 0) {
            listState.animateScrollToItem(
                index = index,
                scrollOffset = -centerOffset
            )
        }
    }

    Column(
        modifier = Modifier
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

            itemsIndexed(daysRange) { index, date ->
                val isSelected = date == selectedDate
                val label = getDateLabel(date, today)

                Column(
                    modifier = Modifier
                        .clickable {
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

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    }
}


@Composable
fun getDateLabel(date: LocalDate, today: LocalDate): String {
    // para idioma
    val localizedContext = LocalizedContext.current

    val formatterDay = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())
    val formatterMonth = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())

    return when {
        date == today.minusDays(1) -> localizedContext.getString(R.string.matches_screen_yesterday_title)
        date == today -> localizedContext.getString(R.string.matches_screen_today_title)
        date == today.plusDays(1) -> localizedContext.getString(R.string.matches_screen_tomorrow_title)
        else -> "${date.format(formatterDay)} ${date.format(formatterMonth)}"
    }.replaceFirstChar { it.uppercaseChar() }
}