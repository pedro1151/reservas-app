package com.optic.pramosreservasappz.presentation.screens.salecomplete.steptree

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel

@Composable
fun CompleteSaleStepTreeContent(
    selectedProducts: List<Pair<ProductResponse, Int>>,
    paddingValues: PaddingValues,
    total: Double,
    totalItems: Int,
    viewModel: SalesViewModel,
    navController:NavHostController
) {

    val Cyan = Color(0xFF22C1C3)
    val CyanSoft = Color(0xFF22C1C3).copy(alpha = 0.12f)

    val TextPrimary = Color(0xFF0F172A)
    val TextSecondary = Color(0xFF475569)
    val BorderSoft = Color(0xFFE2E8F0)
    val BackgroundSoft = Color(0xFFF8FAFC)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(BackgroundSoft),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {

        // 🔹 HEADER
        item {

            Spacer(Modifier.height(6.dp))

            Text(
                text = "$totalItems productos",
                fontSize = 15.sp,
                color = TextSecondary
            )

            Spacer(Modifier.height(18.dp))
        }

        // 🔹 ITEMS
        items(selectedProducts) { (product, quantity) ->

            val subtotal = product.price * quantity

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black.copy(alpha = 0.05f),
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🔥 NUEVO BADGE (MUCHO mejor)
                Box(
                    modifier = Modifier
                        .background(
                            color = CyanSoft,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$quantity x",
                        fontWeight = FontWeight.Bold,
                        color = Cyan,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "$ ${product.price}",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }

                // 🔥 SUBTOTAL destacado
                Text(
                    text = "$ ${"%.2f".format(subtotal)}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
            }

            // 🔥 DIVIDER MODERNO (no bloque pesado)
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderSoft)
            )
        }

        item { Spacer(Modifier.height(10.dp)) }

        // 🔥 NOMBRE DE LA VENTA
        item {
            if (viewModel.saleName != null) {

                Text(
                    text = "Nombre de la Venta",
                    fontSize = 16.sp,
                    color = TextSecondary
                )

                Spacer(Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color.Black.copy(alpha = 0.05f),
                            spotColor = Color.Black.copy(alpha = 0.08f)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp)) // 🔥 FIX
                            .background(Color.White)
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                    ) {

                        Text(
                            viewModel.saleName,
                            fontSize = 16.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
        item { Spacer(Modifier.height(10.dp)) }

        // 🔥 CLIENTE SELECCIONADO
        item {


                    // 🔹 HEADER (Cliente + acción)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp),
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
                            color = Cyan,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(ClientScreen.SelecClient.route)
                                }
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // 🔹 CONTENIDO
                    if (viewModel.selectedClientId != null) {

                        // ✅ CLIENTE SELECCIONADO
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 12.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    ambientColor = Color.Black.copy(alpha = 0.05f),
                                    spotColor = Color.Black.copy(alpha = 0.08f)
                                )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp)) // 🔥 FIX sombra interna
                                    .background(Color.White)
                                    .padding(horizontal = 20.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                // 🔹 Avatar
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(CyanSoft),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = viewModel.selectedClientName
                                            ?.take(2)
                                            ?.uppercase() ?: "",
                                        color = Cyan,
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
                                        fontWeight = FontWeight.Medium,
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
                        }

                    } else {

                        // ❌ SIN CLIENTE → CTA CARD
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    ambientColor = Color.Black.copy(alpha = 0.04f),
                                    spotColor = Color.Black.copy(alpha = 0.06f)
                                )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .clickable {
                                        navController.navigate(ClientScreen.SelecClient.route)
                                    }
                                    .padding(horizontal = 20.dp, vertical = 18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(CyanSoft),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "+",
                                        color = Cyan,
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
                }

        item { Spacer(Modifier.height(10.dp)) }
        // 🔥 METODO DE PAGO
        item {
            if (viewModel.paymentMethod != null) {

                Text(
                    text = "Metodo de Pago",
                    fontSize = 16.sp,
                    color = TextSecondary
                )

                Spacer(Modifier.height(6.dp))

                val scale by animateFloatAsState(
                    targetValue = 1.05f,
                    animationSpec = tween(180),
                    label = ""
                )

                val checkScale by animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = tween(220),
                    label = ""
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color.Black.copy(alpha = 0.05f),
                            spotColor = Color.Black.copy(alpha = 0.08f)
                        )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp)) // 🔥 FIX CLAVE
                            .background(Color.White)
                            .padding(horizontal = 100.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                                .clip(RoundedCornerShape(14.dp))
                                .background(CyanSoft)
                                .border(1.4.dp, Cyan, RoundedCornerShape(14.dp))
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Box(Modifier.fillMaxSize()) {

                                Column(
                                    modifier = Modifier.align(Alignment.Center),
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
                                        tint = Cyan
                                    )

                                    Spacer(Modifier.height(6.dp))

                                    Text(
                                        viewModel.paymentMethod ?: "No seleccionado",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Cyan
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
                                        .background(Cyan, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(11.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


        item { Spacer(Modifier.height(10.dp)) }
        // 🔥 TOTAL
        item {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black.copy(alpha = 0.05f),
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)) // 🔥 FIX
                        .background(Color.White)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
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
                        color = Cyan
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(90.dp))
        }
    }

}