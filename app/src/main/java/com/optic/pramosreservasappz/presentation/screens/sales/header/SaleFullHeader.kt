package com.optic.pramosreservasappz.presentation.screens.sales.header


import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
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
import com.optic.pramosreservasappz.presentation.ui.theme.GrisSuave
import com.optic.pramosreservasappz.presentation.ui.theme.SoftCoolBackground

@Composable
fun SaleFullHeader(
    todayTotal:     Double,
    todayCount:     Int,
    yesterdayTotal: Double,
    monthTotal:     Double,
    balanceHidden:  Boolean,
    onToggleHide:   () -> Unit,
    navController: NavHostController,
    listState: LazyListState
) {



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
                        color = Color.Black.copy(alpha = 0.05f)
                    )
                }
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Texto "HOY"
                    Text(
                        "HOY",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        color = MaterialTheme.colorScheme.background,
                        letterSpacing = 1.5.sp
                    )

                    Spacer(Modifier.width(10.dp))

                    // Row que contiene ventas y el icono de ojo
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Texto de ventas
                        Text(
                            " $todayCount ${if (todayCount == 1) "ventas" else "ventas"}",
                            fontSize = 15.sp,
                            color =AmarrilloSuave,
                            fontWeight = FontWeight.W700
                        )

                        Spacer(Modifier.width(6.dp)) // separa el texto del icono

                        // Icono de visibilidad
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
                                tint = GrisModerno,
                                modifier = Modifier.size(16.dp)
                            )
                        }
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
                        fontSize = 35.sp,
                        fontWeight = FontWeight.W700,
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                        letterSpacing = (1).sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally), modifier = Modifier.fillMaxWidth()) {
                    StatPill(label = "Ayer",     value = if (balanceHidden) "••••" else "$ ${"%.0f".format(yesterdayTotal)}")
                    StatPill(label = "Este mes", value = if (balanceHidden) "••••" else "$ ${"%.0f".format(monthTotal)}")
                }

                Spacer(Modifier.height(20.dp))

                val ButtonOnCyan = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF0F172A), // negro con azul
                        Color(0xFF1E293B)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // 🔥 BOTÓN 1 — Venta rápida
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ButtonBackground)
                            .drawBehind {
                                drawRect(
                                    color = Color.Black.copy(alpha = 0.05f)
                                )
                            }
                            .clickable {
                                navController.navigate(ClientScreen.RapidSale.route)
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Bolt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                                modifier = Modifier.size(22.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                "Venta rápida",
                                fontWeight = FontWeight.W700,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                            )
                            /*
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.AddShoppingCart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                                modifier = Modifier.size(22.dp)
                            )

                             */

                        }
                    }


                    // 🔥 BOTÓN 2 — Venta completa
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(GradientBackground)
                            .drawBehind {
                                drawRect(
                                    color = Color.Black.copy(alpha = 0.05f)
                                )
                            }
                            .clickable {
                                navController.navigate(ClientScreen.RapidSale.route)
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.AddTask,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                                modifier = Modifier.size(22.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                "Venta completa",
                                fontWeight = FontWeight.W700,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                            )
                            /*
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.AddShoppingCart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                                modifier = Modifier.size(22.dp)
                            )

                             */

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
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label,
            fontSize = 13.sp,
            color =  AmarrilloSuave,
            fontWeight = FontWeight.W700,
        )
        Spacer(Modifier.height(2.dp))
        Text(value,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
            fontWeight = FontWeight.W600
        )
    }
}


