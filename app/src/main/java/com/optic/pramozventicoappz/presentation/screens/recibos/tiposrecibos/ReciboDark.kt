package com.optic.pramozventicoappz.presentation.screens.recibos.tiposrecibos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramozventicoappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramozventicoappz.presentation.util.formatSaleDate
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ReciboDark(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    // 🎨 DARK MODERNO
    val bg = Color(0xFF0B0F19)
    val card = Color(0xFF121826)
    val cardSoft = Color(0xFF1A2233)
    val border = Color(0xFF263244)

    val text = Color(0xFFF8FAFC)
    val sub = Color(0xFF94A3B8)

    val primary = Color(0xFFE91E63)
    val primarySoft = Color(0xFFFCE4EC)

    val success = Color(0xFF22C55E)
    val whatsapp = Color(0xFF25D366)
    val blue = Color(0xFF3B82F6)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = paddingValues.calculateTopPadding() + 14.dp,
                start = 14.dp,
                end = 14.dp,
                bottom = 30.dp
            ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // 🔥 RECIBO


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = card),
                border = BorderStroke(1.dp, border),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    // HEADER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            primary,
                                            Color(0xFFD81B60)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (business?.name ?: "MN")
                                    .take(2)
                                    .uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = business?.name ?: "Mi Negocio",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = text
                            )

                            Text(
                                text = listOfNotNull(
                                    business?.city,
                                    business?.country
                                ).joinToString(", "),
                                fontSize = 12.sp,
                                color = sub
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(primary.copy(alpha = 0.12f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "#${sale.sale.id}",
                                color = primary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    Divider(color = border)

                    Spacer(Modifier.height(16.dp))

                    // INFO EN GRID
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        InfoDarkBox(
                            modifier = Modifier.weight(1f),
                            label = "Fecha",
                            value = formatSaleDate(sale.sale.created),
                            bg = cardSoft,
                            labelColor = sub,
                            valueColor = text
                        )

                        InfoDarkBox(
                            modifier = Modifier.weight(1f),
                            label = "Pago",
                            value = sale.sale.paymentMethod ?: "N/A",
                            bg = cardSoft,
                            labelColor = sub,
                            valueColor = text
                        )
                    }

                    if (!business?.phone.isNullOrBlank()) {
                        Spacer(Modifier.height(10.dp))

                        InfoDarkBox(
                            modifier = Modifier.fillMaxWidth(),
                            label = "Contacto",
                            value = business?.phone ?: "",
                            bg = cardSoft,
                            labelColor = sub,
                            valueColor = text
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = "DETALLE",
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = sub,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(12.dp))

                    // ITEMS
                    sale.items.forEach { item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(cardSoft)
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(primary.copy(alpha = 0.10f))
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 6.dp
                                    )
                            ) {
                                Text(
                                    text = "${item.quantity}x",
                                    color = primary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = item.product?.name ?: "Producto",
                                    color = text,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "$ ${
                                        "%.2f".format(item.price)
                                    } c/u",
                                    color = sub,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = "$ ${
                                    "%.2f".format(
                                        item.price * item.quantity
                                    )
                                }",
                                color = text,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(10.dp))
                    }

                    Spacer(Modifier.height(8.dp))

                    Divider(color = border)

                    Spacer(Modifier.height(16.dp))

                    // TOTAL
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "TOTAL PAGADO",
                                fontSize = 11.sp,
                                color = sub,
                                letterSpacing = 1.sp
                            )

                            Text(
                                text = "Gracias por su compra",
                                fontSize = 12.sp,
                                color = sub
                            )
                        }

                        Text(
                            text = "$ ${
                                "%.2f".format(sale.sale.amount)
                            }",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = success
                        )
                    }
                }
            }



    }
}

@Composable
private fun InfoDarkBox(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    bg: Color,
    labelColor: Color,
    valueColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .padding(14.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = labelColor
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 13.sp,
            color = valueColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}
