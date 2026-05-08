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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.optic.pramozventicoappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramozventicoappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramozventicoappz.presentation.util.formatSaleDate

@Composable
fun ReciboCosmetic(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    // 🎀 PALETA COSMETIC
    val bg = Color(0xFFFFF7FB)
    val card = Color.White
    val soft = Color(0xFFFFEEF6)
    val border = Color(0xFFF7D7E7)

    val primary = Color(0xFFE91E63)
    val primaryDark = Color(0xFFC2185B)
    val lavender = Color(0xFFD8B4FE)
    val peach = Color(0xFFFFD6C9)

    val text = Color(0xFF3C2A38)
    val sub = Color(0xFF8B6E7C)
    val success = Color(0xFF16A34A)


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

        // 🌸 RECIBO PRINCIPAL


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = card),
                border = BorderStroke(1.dp, border),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    // HEADER BONITO
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        primary,
                                        primaryDark
                                    )
                                )
                            )
                            .padding(18.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (business?.name ?: "MC")
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
                                    text = business?.name ?: "Beauty Store",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = listOfNotNull(
                                        business?.city,
                                        business?.country
                                    ).joinToString(", "),
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = "#${sale.sale.id}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // INFO
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        CosmeticInfoBox(
                            modifier = Modifier.weight(1f),
                            title = "Fecha",
                            value = formatSaleDate(sale.sale.created),
                            bg = soft,
                            titleColor = sub,
                            valueColor = text
                        )

                        CosmeticInfoBox(
                            modifier = Modifier.weight(1f),
                            title = "Pago",
                            value = sale.sale.paymentMethod ?: "N/A",
                            bg = soft,
                            titleColor = sub,
                            valueColor = text
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = "PRODUCTOS",
                        color = sub,
                        fontSize = 11.sp,
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
                                .background(soft)
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(lavender.copy(alpha = 0.35f))
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 5.dp
                                    )
                            ) {
                                Text(
                                    text = "${item.quantity}x",
                                    color = primaryDark,
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
                                    color = text,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = "$ ${"%.2f".format(item.price)} c/u",
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
                                color = primaryDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(Modifier.height(10.dp))
                    }

                    Spacer(Modifier.height(8.dp))

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
                                text = "TOTAL",
                                color = sub,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp
                            )

                            Text(
                                text = "Gracias por elegirnos ✨",
                                color = sub,
                                fontSize = 12.sp
                            )
                        }

                        Text(
                            text = "$ ${"%.2f".format(sale.sale.amount)}",
                            color = success,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp
                        )
                    }
                }
            }


    }
}

@Composable
private fun CosmeticInfoBox(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    bg: Color,
    titleColor: Color,
    valueColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
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

