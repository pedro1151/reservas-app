package com.optic.pramosreservasappz.presentation.screens.sales.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.domain.model.sales.SaleWithItemsResponse

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.optic.pramosreservasappz.presentation.screens.historial.components.getAvatarColor
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno
import com.optic.pramosreservasappz.presentation.util.formatSaleDate

@Composable
fun SaleDetailContent(
    sale: SaleWithItemsResponse,
    modifier: Modifier = Modifier
) {

    val greenGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF10B981), // verde más claro
            Color(0xFF059669), // verde base
        )
    )

    val accentGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF06B6D4))
    )

    val itemGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2)) // fondo suave para items
    )

    val clientHardcode = mapOf(
        "Nombre" to "Pedro Ticona Ramos",
        "Email" to "cliente@ejemplo.com",
        "Teléfono" to "+591 71234567"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(horizontal = 12.dp, vertical = 60.dp)
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            item {
                // 💳 Card principal de la venta
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "#: ${sale.sale.id}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black.copy(alpha = 0.85f)
                            )
                            Text(
                                formatSaleDate(sale.sale.created),
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = sale.sale.description ?: "Sin descripción",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )

                        Spacer(Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Método: ${sale.sale.paymentMethod}",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                            val amount: Double = sale.sale.amount
                            Text(
                                "$${"%.2f".format(amount)}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .background(greenGradient, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            item {
                // 👤 Card de Cliente
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Cliente",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(20.dp))
                        clientHardcode.forEach { (label, value) ->
                            Text(
                                "$label: $value",
                                fontSize = 16.sp,
                                color = GrisModerno
                            )
                        }
                    }
                }
            }

            item {
                // 🛒 Header Productos
                Text(
                    "Productos/Servicios incluidos",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.9f),
                    modifier = Modifier.padding(bottom = 8.dp, top = 4.dp)
                )
            }

            items(sale.items) { item ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }

                val avatarColor = remember(item.product?.id) { getAvatarColor(item.product?.id?:0) }
                val initials = "#"+item.product?.id.toString()

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(250)) + expandVertically(tween(250)),
                    exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFEEEEEE))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 8.dp)
                        ) {
                            // Fila principal: avatar + info + tres puntos
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Avatar con barra de color
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(avatarColor),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = initials,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.background
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                item.product?.name ?: "Producto desconocido",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 15.sp,
                                                color = Color.Black
                                            )
                                            Text(
                                                "${item.quantity} x $${"%.2f".format(item.price)}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.Gray
                                            )
                                        }
                                        val total = item.quantity * item.price
                                        Text(
                                            "$${"%.2f".format(total)}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color =   Color(0xFF059669)
                                            //  Color(0xFF4A6CF7), Color(0xFF7C3AED), Color(0xFF059669),
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}