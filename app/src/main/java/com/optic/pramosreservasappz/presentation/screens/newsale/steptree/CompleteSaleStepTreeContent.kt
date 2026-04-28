package com.optic.pramosreservasappz.presentation.screens.newsale.steptree

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.inicio.SalesViewModel

@Composable
fun CompleteSaleStepTreeContent(
    selectedProducts: List<Pair<ProductResponse, Int>>,
    paddingValues: PaddingValues,
    total: Double,
    totalItems: Int,
    viewModel: SalesViewModel,
    navController: NavHostController
) {

    // 🎨 NUEVA PALETA
    val Primary = Color(0xFFE91E63)
    val PrimarySoft = Color(0xFFFCE4EC)

    val TextPrimary = Color(0xFF1F2937)
    val TextSecondary = Color(0xFF6B7280)

    val BorderNeutral = Color(0xFFE5E7EB)
    val Surface = Color.White
    val SurfaceSoft = Color(0xFFFFF7FA)

    val DangerSoft = Color(0xFFFFEBEE)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(SurfaceSoft),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {

        // HEADER
        item {

            Spacer(Modifier.height(6.dp))

            Text(
                text = "$totalItems productos",
                fontSize = 15.sp,
                color = TextSecondary
            )

            Spacer(Modifier.height(18.dp))
        }

        // ITEMS
        items(selectedProducts) { (product, quantity) ->

            val subtotal = product.price * quantity

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Surface)
                    .border(1.dp, BorderNeutral, RoundedCornerShape(18.dp))
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .background(
                            color = PrimarySoft,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$quantity x",
                        fontWeight = FontWeight.Bold,
                        color = Primary,
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
                        fontSize = 15.sp,
                        color = TextPrimary
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "$ ${product.price}",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }

                Text(
                    text = "$ ${"%.2f".format(subtotal)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
            }

            Spacer(Modifier.height(10.dp))
        }

        // NOMBRE VENTA
        item {
            if (viewModel.saleName != null) {

                Text(
                    text = "Nombre de la Venta",
                    fontSize = 16.sp,
                    color = TextSecondary
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Surface)
                        .border(1.dp, BorderNeutral, RoundedCornerShape(18.dp))
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    Text(
                        viewModel.saleName,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                }
            }
        }

        item { Spacer(Modifier.height(12.dp)) }

        // CLIENTE
        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Cliente",
                    fontSize = 16.sp,
                    color = TextSecondary
                )

                Text(
                    text = if (viewModel.selectedClientId == null) "Agregar cliente" else "Modificar",
                    fontSize = 14.sp,
                    color = Primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        navController.navigate(ClientScreen.SelecClient.route)
                    }
                )
            }

            Spacer(Modifier.height(10.dp))

            if (viewModel.selectedClientId != null) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Surface)
                        .border(1.dp, BorderNeutral, RoundedCornerShape(18.dp))
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PrimarySoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = viewModel.selectedClientName
                                ?.take(2)
                                ?.uppercase() ?: "",
                            color = Primary,
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
                            color = TextPrimary
                        )

                        val secondary = viewModel.selectedClientEmail
                            ?: viewModel.selectedClientPhone

                        if (secondary != null) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = secondary,
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

            } else {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Surface)
                        .border(1.dp, BorderNeutral, RoundedCornerShape(18.dp))
                        .clickable {
                            navController.navigate(ClientScreen.SelecClient.route)
                        }
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PrimarySoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "+",
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "Agregar cliente a la venta",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        item { Spacer(Modifier.height(12.dp)) }

        // MÉTODO PAGO
        item {
            if (viewModel.paymentMethod != null) {

                Text(
                    text = "Método de Pago",
                    fontSize = 16.sp,
                    color = TextSecondary
                )

                Spacer(Modifier.height(6.dp))

                val scale by animateFloatAsState(
                    targetValue = 1.04f,
                    animationSpec = tween(180),
                    label = ""
                )

                val checkScale by animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = tween(220),
                    label = ""
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Surface)
                        .border(1.dp, BorderNeutral, RoundedCornerShape(18.dp))
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                            .clip(RoundedCornerShape(14.dp))
                            .background(PrimarySoft)
                            .border(1.dp, PrimarySoft, RoundedCornerShape(14.dp))
                            .padding(horizontal = 22.dp, vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Box {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
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
                                    tint = Primary
                                )

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    viewModel.paymentMethod ?: "",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Primary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 6.dp, y = (-6).dp)
                                    .size(18.dp)
                                    .graphicsLayer {
                                        scaleX = checkScale
                                        scaleY = checkScale
                                    }
                                    .background(Primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    null,
                                    tint = Color.White,
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(14.dp)) }

        // TOTAL
        item {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Surface)
                    .border(1.dp, BorderNeutral, RoundedCornerShape(20.dp))
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "Total",
                    fontSize = 16.sp,
                    color = TextSecondary
                )

                Text(
                    "$ ${"%.2f".format(total)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }
        }

        item {
            Spacer(Modifier.height(90.dp))
        }
    }
}