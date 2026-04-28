package com.optic.pramosreservasappz.presentation.screens.newsale.steptree

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.model.sales.types.SaleType
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel

@Composable
fun CompleteSaleStepTreeContent(
    selectedProducts: List<Pair<ProductResponse, Int>>,
    paddingValues: PaddingValues,
    total: Double,
    totalItems: Int,
    viewModel: NewSaleViewModel,
    navController: NavHostController
) {

    val primary = MaterialTheme.colorScheme.primary
    val primarySoft = MaterialTheme.colorScheme.primaryContainer

    val textPrimary = Color(0xFF0F172A)
    val textSecondary = Color(0xFF475569)

    val borderSoft = Color(0xFFE2E8F0)
    val surface = Color.White
    val background = MaterialTheme.colorScheme.background

    val isRapidSale = viewModel.saleFlowType == SaleType.RAPID

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(background)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            if (!isRapidSale) {
                item {
                    SectionTitle("Nombre de la venta", textSecondary)

                    Spacer(Modifier.height(8.dp))

                    InfoContainer(
                        borderSoft = borderSoft,
                        surface = surface
                    ) {
                        Text(
                            text = viewModel.saleName,
                            fontSize = 15.sp,
                            color = textPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            item {
                Column {
                    Text(
                        text = "$totalItems Productos",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary
                    )

                }
            }

            items(selectedProducts) { (product, quantity) ->

                val subtotal = product.price * quantity

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surface)
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            drawLine(
                                color = borderSoft,
                                start = androidx.compose.ui.geometry.Offset(0f, size.height),
                                end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                                strokeWidth = strokeWidth
                            )
                        }
                        .padding(horizontal = 4.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .background(primarySoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${quantity}x",
                            fontWeight = FontWeight.Bold,
                            color = primary,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = product.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = "$ ${"%.2f".format(product.price)} c/u",
                            fontSize = 12.sp,
                            color = textSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = "$ ${"%.2f".format(subtotal)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = textPrimary
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SectionTitle("Cliente", textSecondary)

                    Text(
                        text = if (viewModel.selectedClientId == null) "Agregar cliente" else "Modificar",
                        fontSize = 14.sp,
                        color = primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            navController.navigate(ClientScreen.SelecClient.route)
                        }
                    )
                }

                Spacer(Modifier.height(10.dp))

                if (viewModel.selectedClientId != null) {

                    InfoContainer(
                        borderSoft = borderSoft,
                        surface = surface
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(primarySoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = viewModel.selectedClientName
                                        ?.take(2)
                                        ?.uppercase() ?: "",
                                    color = primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = viewModel.selectedClientName ?: "",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textPrimary
                                )

                                val secondary = viewModel.selectedClientEmail
                                    ?: viewModel.selectedClientPhone

                                if (secondary != null) {
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = secondary,
                                        fontSize = 13.sp,
                                        color = textSecondary
                                    )
                                }
                            }
                        }
                    }

                } else {

                    InfoContainer(
                        borderSoft = borderSoft,
                        surface = surface,
                        modifier = Modifier.clickable {
                            navController.navigate(ClientScreen.SelecClient.route)
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(primarySoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "+",
                                    color = primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = "Agregar cliente a la venta",
                                fontSize = 14.sp,
                                color = textSecondary
                            )
                        }
                    }
                }
            }

            item {
                if (viewModel.paymentMethod != null) {

                    SectionTitle("Método de pago", textSecondary)

                    Spacer(Modifier.height(8.dp))

                    val scale by animateFloatAsState(
                        targetValue = 1.02f,
                        animationSpec = tween(180),
                        label = "payment_scale"
                    )

                    InfoContainer(
                        borderSoft = borderSoft,
                        surface = surface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(primarySoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (viewModel.paymentMethod) {
                                        "Efectivo" -> Icons.Default.AttachMoney
                                        "Tarjeta Débito" -> Icons.Default.CreditCard
                                        "Tarjeta Crédito" -> Icons.Default.CreditScore
                                        "Mercado Pago" -> Icons.Default.Memory
                                        "Transferencia" -> Icons.Default.AccountBalance
                                        else -> Icons.Default.AccountBalance
                                    },
                                    contentDescription = null,
                                    tint = primary
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = viewModel.paymentMethod ?: "",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary,
                                modifier = Modifier.weight(1f)
                            )

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(20.dp))
            }
        }

        TotalSummaryBar(
            total = total,
            totalItems = totalItems,
            primary = primary,
            primarySoft = primarySoft,
            textPrimary = textPrimary,
            textSecondary = textSecondary,
            surface = surface,
            borderSoft = borderSoft
        )
    }
}

@Composable
private fun TotalSummaryBar(
    total: Double,
    totalItems: Int,
    primary: Color,
    primarySoft: Color,
    textPrimary: Color,
    textSecondary: Color,
    surface: Color,
    borderSoft: Color
) {
    Surface(
        shadowElevation = 10.dp,
        color = surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(surface)
                .border(
                    width = 1.dp,
                    color = borderSoft.copy(alpha = 0.7f)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = "Total",
                    fontSize = 16.sp,
                    color = textPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = "$totalItems productos",
                    fontSize = 12.sp,
                    color = textSecondary
                )
            }

            Text(
                text = "$ ${"%.2f".format(total)}",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = primary
            )
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    color: Color
) {
    Text(
        text = title,
        fontSize = 15.sp,
        color = color,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun InfoContainer(
    borderSoft: Color,
    surface: Color,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(surface)
            .border(1.dp, borderSoft, RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 15.dp),
        content = content
    )
}