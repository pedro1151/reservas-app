package com.optic.pramosfootballappz.presentation.screens.prode.leagueprodefixtures.header


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.optic.pramosfootballappz.domain.model.administracion.LeagueRound
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun RoundSelector(
    rounds: List<LeagueRound>,
    modifier: Modifier = Modifier,
    onRoundSelected: (LeagueRound) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val currentRound = remember(rounds) {
        rounds.firstOrNull { it.isCurrent } ?: rounds.firstOrNull()
    }

    var selectedRound by remember {
        mutableStateOf(currentRound)
    }

    Box(modifier = modifier) {

        // ---------- SELECTOR ----------
        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = selectedRound?.roundName ?: "Seleccionar round",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.rotate(
                    animateFloatAsState(
                        targetValue = if (expanded) 180f else 0f,
                        label = "arrow"
                    ).value
                )
            )
        }

        // ---------- DROPDOWN ----------
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            rounds.forEachIndexed { index, round ->

                val isFuture = round.startDate > currentRound?.startDate.orEmpty()
                val isCurrent = round.isCurrent
                val delay = index * 3

                AnimatedVisibility(
                    visible = expanded,
                    enter = EnterTransition.None, // 👈 INMEDIATO
                    exit = fadeOut(
                        animationSpec = tween(durationMillis = 80)
                    ) + slideOutVertically(
                        targetOffsetY = { it / 4 }
                    )
                ){
                    DropdownMenuItem(
                        enabled = !isFuture,
                        onClick = {
                            selectedRound = round
                            expanded = false
                            onRoundSelected(round)
                        },
                        contentPadding = PaddingValues(
                            horizontal = 20.dp,
                        ),
                        text = {
                            Column {

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = round.roundName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = when {
                                            isCurrent -> MaterialTheme.colorScheme.primary
                                            isFuture -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
                                    )

                                    if (isCurrent) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        AssistChip(
                                            onClick = {},
                                            label = {
                                                Text(
                                                    text = "Actual",
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "${formatRoundDate(round.startDate)} – ${formatRoundDate(round.endDate)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}


fun formatRoundDate(date: String): String {
    return try {
        val parsed = LocalDate.parse(date)
        parsed.format(
            DateTimeFormatter.ofPattern("d MMM yyyy", Locale("es"))
        )
    } catch (e: Exception) {
        date
    }
}




