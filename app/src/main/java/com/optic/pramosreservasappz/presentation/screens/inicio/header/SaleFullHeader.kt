// SaleFullHeader.kt
package com.optic.pramosreservasappz.presentation.screens.inicio.header

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.sales.types.SaleType
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel

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
    onMenuClick: () -> Unit,
    newSaleViewModel: NewSaleViewModel
) {

    val Primary = Color(0xFFE91E63)
    val PrimaryDark = Color(0xFFD81B60)
    val Accent =Color(0xFFFFC107)
    val White = Color.White
    val Muted = Color.White.copy(alpha = 0.75f)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Primary,
                            PrimaryDark
                        )
                    )
                )
                .padding(horizontal = 14.dp, vertical = 16.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = onMenuClick) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = null,
                            tint = White
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "HOY",
                        color = Muted,
                        letterSpacing = 2.sp
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "$todayCount ventas",
                        color = Accent
                    )

                    Spacer(Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onToggleHide() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (balanceHidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                AnimatedContent(
                    targetState = balanceHidden to todayTotal,
                    transitionSpec = {
                        fadeIn(tween(220)) togetherWith fadeOut(tween(150))
                    },
                    label = ""
                ) { (hidden, total) ->


                    Text(
                        text = if (hidden) "$ ••••" else "$ ${"%.2f".format(total)}",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.W800,
                        color = Color.White.copy(alpha = 0.90f),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatPill("Ayer", yesterdayTotal, balanceHidden, Accent)
                    StatPill("Mes", monthTotal, balanceHidden, Accent)
                }

                Spacer(Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .clickable {
                                newSaleViewModel.updateSaleFlowType(SaleType.RAPID)
                                navController.navigate(ClientScreen.CompleteSaleStepTwo.route)
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Venta rápida",
                                color = Color.Black
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                1.dp,
                                White.copy(alpha = 0.35f),
                                RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                newSaleViewModel.updateSaleFlowType(SaleType.COMPLETE)
                                navController.navigate(ClientScreen.CompleteSaleStepOne.route)
                            }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AddTask,
                                contentDescription = null,
                                tint = White
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Venta completa",
                                color = White
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
            text = label,
            color = accent,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = if (hidden) "••••" else "$ ${"%.0f".format(value)}",
            color = Color.White
        )
    }
}