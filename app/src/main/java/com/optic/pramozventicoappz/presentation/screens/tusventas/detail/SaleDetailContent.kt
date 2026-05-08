package com.optic.pramozventicoappz.presentation.screens.tusventas.detail

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramozventicoappz.presentation.screens.tusventas.components.getAvatarColor
import com.optic.pramozventicoappz.presentation.util.formatSaleDate

@Composable
fun SaleDetailContent(
    sale: SaleWithItemsResponse,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    // ── Paleta: mínima, blanco/negro, sin gradientes ──
    val bg          = Color(0xFFF5F5F5)
    val cardBg      = Color.White
    val border      = Color(0xFFEEEEEE)
    val textPrimary = Color.Black
    val textMuted   = Color(0xFF888888)

    // Datos de cliente hardcoded (igual que el original)
    val clientHardcode = linkedMapOf(
        "Nombre"   to "Pedro Ticona Ramos",
        "Email"    to "cliente@ejemplo.com",
        "Teléfono" to "+591 71234567"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(bg),
        contentPadding = PaddingValues(
            top    = paddingValues.calculateTopPadding() + 16.dp,
            start  = 16.dp,
            end    = 16.dp,
            bottom = 40.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ── 1. Card principal de la venta ──
        item {
            Surface(
                modifier        = Modifier.fillMaxWidth(),
                shape           = RoundedCornerShape(16.dp),
                color           = cardBg,
                shadowElevation = 0.dp,
                border          = BorderStroke(0.5.dp, border)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // ID + fecha
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text       = "#${sale.sale.id}",
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = textMuted
                        )
                        Text(
                            text     = formatSaleDate(sale.sale.created),
                            fontSize = 12.sp,
                            color    = textMuted
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    // Descripción
                    Text(
                        text      = sale.sale.description ?: "Sin descripción",
                        fontSize  = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color     = textPrimary,
                        maxLines  = 2,
                        overflow  = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(20.dp))

                    // Monto: elemento más prominente
                    Text(
                        text          = "$${"%.2f".format(sale.sale.amount)}",
                        fontSize      = 36.sp,
                        fontWeight    = FontWeight.ExtraBold,
                        color         = textPrimary,
                        letterSpacing = (-1.5).sp
                    )

                    Spacer(Modifier.height(16.dp))

                    // Chips: método de pago + Vendedor (hardcoded per líder)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        DetailChip(label = sale.sale.paymentMethod ?: "N/A")
                        DetailChip(label = "Vendedor")
                    }
                }
            }
        }

        // ── 2. Card cliente ──
        item {
            Surface(
                modifier        = Modifier.fillMaxWidth(),
                shape           = RoundedCornerShape(16.dp),
                color           = cardBg,
                shadowElevation = 0.dp,
                border          = BorderStroke(0.5.dp, border)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    SectionLabel(text = "CLIENTE")
                    Spacer(Modifier.height(16.dp))
                    clientHardcode.entries.forEachIndexed { index, (label, value) ->
                        ClientDetailRow(label = label, value = value)
                        if (index < clientHardcode.size - 1) {
                            HorizontalDivider(
                                modifier  = Modifier.padding(vertical = 10.dp),
                                color     = border,
                                thickness = 0.5.dp
                            )
                        }
                    }
                }
            }
        }

        // ── 3. Cabecera de productos ──
        item {
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                SectionLabel(text = "PRODUCTOS")
                Text(
                    text     = "${sale.items.size} artículos",
                    fontSize = 11.sp,
                    color    = textMuted
                )
            }
        }

        // ── 4. Items de la venta ──
        items(sale.items) { item ->
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }

            val avatarColor = remember(item.product?.id) { getAvatarColor(item.product?.id ?: 0) }

            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(200)) + slideInVertically(tween(220)) { it / 3 }
            ) {
                Surface(
                    modifier        = Modifier.fillMaxWidth(),
                    shape           = RoundedCornerShape(14.dp),
                    color           = cardBg,
                    shadowElevation = 0.dp,
                    border          = BorderStroke(0.5.dp, border)
                ) {
                    Row(
                        modifier          = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar del producto
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(avatarColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text       = "#${item.product?.id}",
                                fontSize   = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = Color.White
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text       = item.product?.name ?: "Producto",
                                fontSize   = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color      = textPrimary,
                                maxLines   = 1,
                                overflow   = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text     = "${item.quantity} × $${"%.2f".format(item.price)}",
                                fontSize = 12.sp,
                                color    = textMuted
                            )
                        }

                        // Total del item en verde
                        Text(
                            text       = "$${"%.2f".format(item.quantity * item.price)}",
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = Color(0xFF059669)
                        )
                    }
                }
            }
        }
    }
}

// ── Helpers de UI ──

@Composable
private fun SectionLabel(text: String) {
    Text(
        text          = text,
        fontSize      = 11.sp,
        fontWeight    = FontWeight.SemiBold,
        color         = Color(0xFF888888),
        letterSpacing = 0.8.sp
    )
}

@Composable
private fun DetailChip(label: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFF0F0F0), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text       = label,
            fontSize   = 12.sp,
            color      = Color(0xFF666666),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ClientDetailRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = Color(0xFFAAAAAA))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}
