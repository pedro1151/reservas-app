package com.optic.pramosreservasappz.presentation.screens.sales.Components

import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaleStatsHeader(
    todayTotal:     Double,
    todayCount:     Int,
    yesterdayTotal: Double,
    monthTotal:     Double,
    balanceHidden:  Boolean,
    onToggleHide:   () -> Unit,
    onRegisterSale: () -> Unit
) {

    val gradient = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary, // azul
            MaterialTheme.colorScheme.secondary, // azul
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 5.dp, vertical = 5.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier              = Modifier.fillMaxWidth()
            ) {
                Text("HOY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onToggleHide),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (balanceHidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        null, tint = Color(0xFF888888), modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            AnimatedContent(
                targetState = balanceHidden to todayTotal,
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(150)) },
                label = "balance"
            ) { (hidden, total) ->
                Text(
                    text = if (hidden) "$ ••••" else "$ ${"%.2f".format(total)}",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.W700,
                    color = SBlack,
                    letterSpacing = (-1).sp
                )
            }

            Spacer(Modifier.height(4.dp))
            Text(" $todayCount ${if (todayCount == 1) "ventas" else "ventas"}",
                fontSize = 13.sp,
                color = SGray600
            )
            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
                StatPill(label = "Ayer",     value = if (balanceHidden) "••••" else "$ ${"%.0f".format(yesterdayTotal)}")
                StatPill(label = "Este mes", value = if (balanceHidden) "••••" else "$ ${"%.0f".format(monthTotal)}")
            }

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(gradient )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, onClick = onRegisterSale
                    )
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp))
                {
                    Icon(Icons.Default.Bolt,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                    Text("Venta rapida",
                        fontSize = 18.sp,
                        //fontWeight = FontWeight.W700,
                        color = Color.White,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label,
            fontSize = 14.sp,
            color = Color(0xFF777777)
        )
        Spacer(Modifier.height(2.dp))
        Text(value,
            fontSize = 15.sp,
            color = SBlack,
            fontWeight = FontWeight.W600
        )
    }
}


