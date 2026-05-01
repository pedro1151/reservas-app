// SaleFullHeader.kt
package com.optic.pramosreservasappz.presentation.screens.inicio.header

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.optic.pramosreservasappz.domain.model.sales.types.SaleType
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary

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
    val primary = MaterialTheme.colorScheme.primary
    val primaryDark = Color(0xFFD81B60)
    val accent = AmarrilloSuave
    val white = Color.White
    val muted = Color.White.copy(alpha = 0.78f)
    val glass = Color.White.copy(alpha = 0.13f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        primary,
                        primaryDark
                    )
                )
            )
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            accent.copy(alpha = 0.28f),
                            Color.Transparent
                        ),
                        radius = size.width * 0.62f
                    ),
                    center = center.copy(
                        x = size.width * 0.08f,
                        y = size.height * 0.05f
                    )
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.16f),
                            Color.Transparent
                        ),
                        radius = size.width * 0.75f
                    ),
                    center = center.copy(
                        x = size.width * 1.04f,
                        y = size.height * 0.42f
                    )
                )
            }
            .padding(horizontal = 16.dp)
            .padding(top = 14.dp, bottom = 18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.13f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onMenuClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = white,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White.copy(alpha = 0.13f))
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hoy",
                        color = white,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.width(7.dp))

                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(accent, CircleShape)
                    )

                    Spacer(Modifier.width(7.dp))

                    Text(
                        text = "$todayCount ventas",
                        color = accent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.13f))
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.18f),
                            shape = CircleShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onToggleHide()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (balanceHidden) {
                            Icons.Outlined.VisibilityOff
                        } else {
                            Icons.Outlined.Visibility
                        },
                        contentDescription = "Ocultar o mostrar saldo",
                        tint = white,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Ingresos de hoy",
                color = muted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.2.sp
            )

            Spacer(Modifier.height(4.dp))

            AnimatedContent(
                targetState = balanceHidden to todayTotal,
                transitionSpec = {
                    fadeIn(tween(220)) togetherWith fadeOut(tween(150))
                },
                label = "todayTotal"
            ) { (hidden, total) ->
                Text(
                    text = if (hidden) "$ ••••••" else "$ ${"%.0f".format(total)}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black,
                    color = white,
                    letterSpacing = (-1.2).sp,
                    lineHeight = 44.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatPill(
                    modifier = Modifier.weight(1f),
                    label = "Ayer",
                    value = yesterdayTotal,
                    hidden = balanceHidden,
                    accent = accent,
                    glass = glass
                )

                StatPill(
                    modifier = Modifier.weight(1f),
                    label = "Este mes",
                    value = monthTotal,
                    hidden = balanceHidden,
                    accent = accent,
                    glass = glass
                )
            }

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .clickable {
                            newSaleViewModel.updateSaleFlowType(SaleType.RAPID)
                            navController.navigate(ClientScreen.CompleteSaleStepTwo.route)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = primary,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(Modifier.width(7.dp))

                        Text(
                            text = "Venta rápida",
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(
                            width = 1.dp,
                            color = white.copy(alpha = 0.28f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            newSaleViewModel.updateSaleFlowType(SaleType.COMPLETE)
                            navController.navigate(ClientScreen.CompleteSaleStepOne.route)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AddTask,
                            contentDescription = null,
                            tint = white,
                            modifier = Modifier.size(19.dp)
                        )

                        Spacer(Modifier.width(7.dp))

                        Text(
                            text = "Completa",
                            color = white,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    modifier: Modifier = Modifier,
    label: String,
    value: Double,
    hidden: Boolean,
    accent: Color,
    glass: Color
) {
    OutlinedCard(
        modifier = modifier.height(68.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = glass
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.16f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.72f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = if (hidden) "$ ••••" else "$ ${"%.0f".format(value)}",
                color = accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
        }
    }
}