package com.optic.pramozventicoappz.presentation.screens.recibos.tiposrecibos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramozventicoappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramozventicoappz.presentation.util.formatSaleDate

@Composable
fun ReciboDrug(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    // 💊 PALETA DRUGSTORE
    val bg = Color(0xFFF3F8F7)
    val white = Color.White
    val border = Color(0xFFD8E6E3)

    val primary = Color(0xFF0F766E)
    val primarySoft = Color(0xFFE6F7F4)

    val accent = Color(0xFF2563EB)
    val success = Color(0xFF16A34A)

    val text = Color(0xFF0F172A)
    val sub = Color(0xFF64748B)

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

        // 💊 RECIBO

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = white),
                border = BorderStroke(1.dp, border),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    // HEADER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(primary, accent)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (business?.name ?: "DR")
                                    .take(2)
                                    .uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = business?.name ?: "Drugstore",
                                fontSize = 18.sp,
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
                                .background(primarySoft)
                                .padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                )
                        ) {
                            Text(
                                text = "#${sale.sale.id}",
                                color = primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // INFO GRID
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        DrugInfoBox(
                            modifier = Modifier.weight(1f),
                            title = "Fecha",
                            value = formatSaleDate(sale.sale.created),
                            bg = primarySoft,
                            titleColor = sub,
                            valueColor = text
                        )

                        DrugInfoBox(
                            modifier = Modifier.weight(1f),
                            title = "Pago",
                            value = sale.sale.paymentMethod ?: "N/A",
                            bg = primarySoft,
                            titleColor = sub,
                            valueColor = text
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    if (!business?.phone.isNullOrBlank()) {
                        DrugInfoBox(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Contacto",
                            value = business?.phone ?: "",
                            bg = Color(0xFFF8FAFC),
                            titleColor = sub,
                            valueColor = text
                        )

                        Spacer(Modifier.height(16.dp))
                    }

                    Text(
                        text = "DETALLE DE PRODUCTOS",
                        fontSize = 11.sp,
                        color = sub,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(12.dp))

                    // ITEMS
                    sale.items.forEach { item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(primarySoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.quantity.toString(),
                                    color = primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = item.product?.name ?: "Producto",
                                    fontSize = 14.sp,
                                    color = text,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "$ ${"%.2f".format(item.price)} c/u",
                                    fontSize = 12.sp,
                                    color = sub
                                )
                            }

                            Text(
                                text = "$ ${
                                    "%.2f".format(
                                        item.price * item.quantity
                                    )
                                }",
                                fontSize = 14.sp,
                                color = text,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(10.dp))
                    }

                    Spacer(Modifier.height(6.dp))

                    Divider(color = border)

                    Spacer(Modifier.height(14.dp))

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
                                text = "Gracias por confiar en nosotros",
                                fontSize = 12.sp,
                                color = sub
                            )
                        }

                        Text(
                            text = "$ ${"%.2f".format(sale.sale.amount)}",
                            fontSize = 28.sp,
                            color = success,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

}

@Composable
private fun DrugInfoBox(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    bg: Color,
    titleColor: Color,
    valueColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .padding(14.dp)
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            color = titleColor
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

