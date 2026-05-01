package com.optic.pramosreservasappz.presentation.screens.newsale.steptree

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.domain.model.sales.types.SaleType
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.newsale.NewSaleViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.ButtonSucessColor
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary

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
    val background = MaterialTheme.colorScheme.background
    val surface = Color.White
    val borderSoft = BorderGray.copy(alpha = 0.75f)

    val isRapidSale = viewModel.saleFlowType == SaleType.RAPID

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(background)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 14.dp,
                bottom = 26.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                SaleInfoSummaryCard(
                    isRapidSale = isRapidSale,
                    saleName = viewModel.saleName,
                    selectedClientId = viewModel.selectedClientId,
                    selectedClientName = viewModel.selectedClientName,
                    selectedClientEmail = viewModel.selectedClientEmail,
                    selectedClientPhone = viewModel.selectedClientPhone,
                    paymentMethod = viewModel.paymentMethod,
                    primary = primary,
                    primarySoft = primarySoft,
                    surface = surface,
                    borderSoft = borderSoft,
                    navController = navController
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Productos",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )

                        Spacer(Modifier.height(2.dp))

                        Text(
                            text = "$totalItems items agregados",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }
            }

            items(
                items = selectedProducts,
                key = { it.first.id }
            ) { (product, quantity) ->

                val subtotal = product.price * quantity

                ProductSummaryRow(
                    product = product,
                    quantity = quantity,
                    subtotal = subtotal,
                    primary = primary,
                    primarySoft = primarySoft,
                    borderSoft = borderSoft,
                    surface = surface
                )
            }

            item {
                Spacer(Modifier.height(90.dp))
            }
        }

        TotalSummaryBar(
            total = total,
            totalItems = totalItems,
            primary = primary,
            surface = surface
        )
    }
}

@Composable
private fun SaleInfoSummaryCard(
    isRapidSale: Boolean,
    saleName: String,
    selectedClientId: Int?,
    selectedClientName: String?,
    selectedClientEmail: String?,
    selectedClientPhone: String?,
    paymentMethod: String?,
    primary: Color,
    primarySoft: Color,
    surface: Color,
    borderSoft: Color,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.08f)
            )
            .background(surface, RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(28.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(primary.copy(alpha = 0.10f))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isRapidSale) "Venta rápida" else "Venta completa",
                    color = primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(Modifier.weight(1f))
        }

        if (!isRapidSale) {
            Spacer(Modifier.height(16.dp))

            SummaryInfoBlock(
                label = "Nombre de venta",
                value = saleName.ifBlank { "Sin nombre" },
                primary = primary
            )
        }

        Spacer(Modifier.height(16.dp))

        ClientSummaryBlock(
            selectedClientId = selectedClientId,
            selectedClientName = selectedClientName,
            selectedClientEmail = selectedClientEmail,
            selectedClientPhone = selectedClientPhone,
            primary = primary,
            primarySoft = primarySoft,
            borderSoft = borderSoft,
            navController = navController
        )

        if (!isRapidSale && paymentMethod != null) {
            Spacer(Modifier.height(14.dp))

            PaymentSummaryBlock(
                paymentMethod = paymentMethod,
                primary = primary,
                primarySoft = primarySoft,
                borderSoft = borderSoft
            )
        }
    }
}

@Composable
private fun SummaryInfoBlock(
    label: String,
    value: String,
    primary: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Bold
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
private fun ClientSummaryBlock(
    selectedClientId: Int?,
    selectedClientName: String?,
    selectedClientEmail: String?,
    selectedClientPhone: String?,
    primary: Color,
    primarySoft: Color,
    borderSoft: Color,
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
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = if (selectedClientId == null) "Agregar" else "Modificar",
                fontSize = 13.sp,
                color = primary,
                fontWeight = FontWeight.Black,
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
                    .background(Color(0xFFFAFBFC))
                    .border(1.dp, borderSoft, RoundedCornerShape(18.dp))
                    .padding(horizontal = 14.dp, vertical = 13.dp)
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
                            text = selectedClientName
                                ?.take(2)
                                ?.uppercase()
                                ?: "",
                            color = primary,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
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
                    .background(Color(0xFFFAFBFC))
                    .border(1.dp, borderSoft, RoundedCornerShape(18.dp))
                    .clickable {
                        navController.navigate(ClientScreen.SelecClient.route)
                    }
                    .padding(horizontal = 14.dp, vertical = 13.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(primary.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = primary,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "Agregar cliente a la venta",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentSummaryBlock(
    paymentMethod: String,
    primary: Color,
    primarySoft: Color,
    borderSoft: Color
) {
    Column {
        Text(
            text = "Método de pago",
            fontSize = 12.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFFAFBFC))
                .border(1.dp, borderSoft, RoundedCornerShape(18.dp))
                .padding(horizontal = 14.dp, vertical = 13.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(primarySoft),
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
                        tint = primary,
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
private fun ProductSummaryRow(
    product: ProductResponse,
    quantity: Int,
    subtotal: Double,
    primary: Color,
    primarySoft: Color,
    borderSoft: Color,
    surface: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(surface)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = borderSoft.copy(alpha = 0.75f),
                    start = androidx.compose.ui.geometry.Offset(0f, size.height),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding(horizontal = 2.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(primarySoft),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${quantity}x",
                fontWeight = FontWeight.Black,
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
            color = TextPrimary
        )
    }
}

@Composable
private fun TotalSummaryBar(
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
                    fontSize = 16.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Black
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
                fontWeight = FontWeight.Black,
                color = primary,
                letterSpacing = (-0.7).sp
            )
        }
    }
}