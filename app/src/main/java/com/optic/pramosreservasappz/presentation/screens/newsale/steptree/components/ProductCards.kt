package com.optic.pramosreservasappz.presentation.screens.newsale.steptree.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.MiniProductResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonSucessColor
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary

// CARD USADAS EN EL RESUMEN DE VENTA
// SE HACE DE ESTA FORMA PARA SEPARAR LOGICA, NO HACER PESADOS LOS COMPOSABLES
@Composable
fun ResumeHeroCard(
    total: Double,
    totalItems: Int,
    borderSoft: Color,
    accent: Color,
    accentSoft: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.035f),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = borderSoft.copy(alpha = 0.65f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(accentSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ReceiptLong,
                    contentDescription = null,
                    tint = Color(0xFF9A6A00),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Resumen de venta",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = "$ ${"%.2f".format(total)}",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    letterSpacing = (-0.7).sp,
                    maxLines = 1
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(accent.copy(alpha = 0.16f))
                    .border(
                        width = 1.dp,
                        color = accent.copy(alpha = 0.28f),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .padding(horizontal = 11.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "$totalItems items",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF7A5500)
                )
            }
        }
    }
}


@Composable
fun ResumenSummaryInfoBlock(
    label: String,
    value: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = value,
            fontSize = 15.sp,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ResumenClientSummaryBlock(
    selectedClientId: Int?,
    selectedClientName: String?,
    selectedClientEmail: String?,
    selectedClientPhone: String?,
    primary: Color,
    borderSoft: Color,
    accentSoft: Color,
    neutralSoft: Color,
    navController: NavHostController
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cliente",
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = if (selectedClientId == null) "Agregar" else "Modificar",
                fontSize = 13.sp,
                color = primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    navController.navigate(ClientScreen.SelecClient.route)
                }
            )
        }

        Spacer(Modifier.height(8.dp))

        if (selectedClientId != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(neutralSoft)
                    .border(1.dp, borderSoft, RoundedCornerShape(18.dp))
                    .padding(horizontal = 14.dp, vertical = 13.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(accentSoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedClientName
                                ?.trim()
                                ?.take(2)
                                ?.uppercase()
                                ?: "",
                            color = Color(0xFF8A6100),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = selectedClientName ?: "",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        val secondary = selectedClientEmail ?: selectedClientPhone

                        if (secondary != null) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = secondary,
                                fontSize = 12.5.sp,
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(neutralSoft)
                    .border(1.dp, borderSoft, RoundedCornerShape(18.dp))
                    .clickable {
                        navController.navigate(ClientScreen.SelecClient.route)
                    }
                    .padding(horizontal = 14.dp, vertical = 13.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(accentSoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = Color(0xFF8A6100),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "Agregar cliente a la venta",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ResumenPaymentSummaryBlock(
    paymentMethod: String,
    borderSoft: Color,
    accent: Color,
    accentSoft: Color,
    neutralSoft: Color
) {
    Column {
        Text(
            text = "Método de pago",
            fontSize = 12.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, borderSoft, RoundedCornerShape(18.dp))
                .padding(horizontal = 14.dp, vertical = 13.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(43.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(accentSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (paymentMethod) {
                            "Efectivo" -> Icons.Default.AttachMoney
                            "Tarjeta Débito" -> Icons.Default.CreditCard
                            "Tarjeta Crédito" -> Icons.Default.CreditScore
                            "Mercado Pago" -> Icons.Default.Memory
                            "Transferencia" -> Icons.Default.AccountBalance
                            else -> Icons.Default.AccountBalance
                        },
                        contentDescription = null,
                        tint = Color(0xFF8A6100),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Text(
                    text = paymentMethod,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(ButtonSucessColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ResumenProductSummaryRow(
    product: MiniProductResponse,
    quantity: Int,
    subtotal: Double,
    borderSoft: Color,
    accentSoft: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = borderSoft.copy(alpha = 0.52f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(accentSoft),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${quantity}x",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8A6100),
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = "$ ${"%.2f".format(product.price)} c/u",
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.width(10.dp))

        Text(
            text = "$ ${"%.2f".format(subtotal)}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = TextPrimary,
            maxLines = 1
        )
    }
}

@Composable
fun ResumenTotalSummaryBar(
    total: Double,
    totalItems: Int,
    primary: Color,
    surface: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor = Color.Black.copy(alpha = 0.10f)
            )
            .background(
                color = surface,
                shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
            )
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Total",
                    fontSize = 15.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = "$totalItems productos",
                    fontSize = 12.5.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "$ ${"%.2f".format(total)}",
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = primary,
                letterSpacing = (-0.7).sp
            )
        }
    }
}
