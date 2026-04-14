package com.optic.pramosreservasappz.presentation.screens.salecomplete.steptree

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.ripple.rememberRipple
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.optic.pramosreservasappz.presentation.screens.sales.SalesViewModel
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno

@Composable
fun CompleteSaleStepTreeContent(
    selectedProducts: List<Pair<ProductResponse, Int>>,
    paddingValues: PaddingValues,
    total: Double,
    totalItems: Int,
    viewModel: SalesViewModel
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
            Text(
                text = "Resumen",
                fontSize = 22.sp, // 🔥 más jerarquía
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "$totalItems productos",
                fontSize = 14.sp,
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

        item { Spacer(Modifier.height(18.dp)) }

        // 🔥 NOMBRE DE LA VENTA
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
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

                    Text(
                        viewModel.saleName,
                        fontSize = 16.sp,
                        color = TextSecondary
                    )

                }
            }
        }

        item { Spacer(Modifier.height(5.dp)) }
        // 🔥 METODO DE PAGO
        item {

            val interaction = remember { MutableInteractionSource() }
            val scale by animateFloatAsState(
                targetValue =  1.05f ,
                animationSpec = tween(180),
                label = ""
            )

            // 🔥 check animado
            val checkScale by animateFloatAsState(
                targetValue = 1f ,
                animationSpec = tween(220),
                label = ""
            )
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
                    .padding(horizontal = 100.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.Center, // 🔥 CENTRADO
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
                        .border(
                            width = 1.4.dp,
                            color = Cyan,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {

                    // 🔥 ESTE BOX CONTROLA EL CHECK CORRECTAMENTE
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {

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
                                text = viewModel.paymentMethod ?: "No seleccionado",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Cyan
                            )
                        }

                        // 🔥 CHECK PERFECTAMENTE EN ESQUINA
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 6.dp, y = (-6).dp) // 🔥 micro ajuste PRO
                                .size(18.dp)
                                .graphicsLayer {
                                    scaleX = checkScale
                                    scaleY = checkScale
                                }
                                .background(
                                    color = Cyan,
                                    shape = CircleShape
                                ),
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


        item { Spacer(Modifier.height(5.dp)) }
        // 🔥 TOTAL BLOCK (más vivo)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
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