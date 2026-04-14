package com.optic.pramosreservasappz.presentation.screens.sales.header

import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonBackground
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno

@Composable
fun SaleFullHeader(
    todayTotal: Double,
    todayCount: Int,
    yesterdayTotal: Double,
    monthTotal: Double,
    balanceHidden: Boolean,
    onToggleHide: () -> Unit,
    navController: NavHostController,
    listState: LazyListState,
    onMenuClick: () -> Unit
) {

    val CyanPrimary = Color(0xFF22C1C3)
    val TextWhite = Color(0xFFF9FAFB)
    val TextMuted = Color(0xFF9CA3AF)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GradientBackground)
                .drawBehind {
                    drawRect(
                        color = Color.Black.copy(alpha = 0.08f)
                    )
                }
                .padding(horizontal = 12.dp, vertical = 14.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                // 🔥 ROW SUPERIOR (MENÚ + HOY CENTRADO)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 🍔 MENU
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    // 🔹 CONTENIDO CENTRO
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            "HOY",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600,
                            color = TextMuted,
                            letterSpacing = 2.sp
                        )

                        Spacer(Modifier.width(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text(
                                "$todayCount ventas",
                                fontSize = 14.sp,
                                color = CyanPrimary,
                                fontWeight = FontWeight.W700
                            )

                            Spacer(Modifier.width(6.dp))

                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = onToggleHide
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (balanceHidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.weight(1f))
                }

                Spacer(Modifier.height(10.dp))

                // 🔹 TOTAL
                AnimatedContent(
                    targetState = balanceHidden to todayTotal,
                    transitionSpec = {
                        fadeIn(tween(220)) togetherWith fadeOut(tween(150))
                    },
                    label = "balance"
                ) { (hidden, total) ->

                    Text(
                        text = if (hidden) "$ ••••" else "$ ${"%.2f".format(total)}",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.W800,
                        color = TextWhite,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(Modifier.height(18.dp))

                // 🔹 STATS
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatPill("Ayer", yesterdayTotal, balanceHidden, CyanPrimary)
                    StatPill("Mes", monthTotal, balanceHidden, CyanPrimary)
                }

                Spacer(Modifier.height(22.dp))

                // 🔹 BOTONES
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ButtonBackground)
                            .clickable {
                                navController.navigate(ClientScreen.RapidSale.route)
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Icon(
                                Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.Black.copy(alpha = 0.85f),
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(Modifier.width(6.dp))

                            Text(
                                "Venta rápida",
                                fontWeight = FontWeight.W700,
                                fontSize = 15.sp,
                                color = Color.Black.copy(alpha = 0.85f),
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Transparent)
                            .border(
                                1.dp,
                                Color.White.copy(alpha = 0.2f),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                navController.navigate(ClientScreen.CompleteSaleStepOne.route)
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Icon(
                                Icons.Default.AddTask,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(Modifier.width(6.dp))

                            Text(
                                "Venta completa",
                                fontWeight = FontWeight.W600,
                                fontSize = 15.sp,
                                color = TextWhite
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    label: String,
    value: Double,
    hidden: Boolean,
    accent: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            label,
            fontSize = 12.sp,
            color = accent.copy(alpha = 0.8f),
            fontWeight = FontWeight.W600
        )

        Spacer(Modifier.height(4.dp))

        Text(
            if (hidden) "••••" else "$ ${"%.0f".format(value)}",
            fontSize = 15.sp,
            color = Color.White,
            fontWeight = FontWeight.W700
        )
    }
}