package com.optic.pramosreservasappz.presentation.screens.recibos.tiposrecibos


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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramosreservasappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramosreservasappz.presentation.util.formatSaleDate

@Composable
fun ReciboPanaderia(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    // 🎨 PALETA PANADERÍA / CONFITERÍA
    val bg = Color(0xFFFFF8F1)
    val cream = Color(0xFFFFFBF7)

    val card = Color.White
    val border = Color(0xFFEADBC8)

    val brown = Color(0xFF6D4C41)
    val brownDark = Color(0xFF4E342E)

    val beige = Color(0xFFD7B899)
    val beigeSoft = Color(0xFFF7EBDD)

    val gold = Color(0xFFD4A373)
    val success = Color(0xFF8D6E63)

    val text = Color(0xFF3E2723)
    val sub = Color(0xFF8D6E63)

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
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // HEADER GRADIENT
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        beige,
                                        gold,
                                        brown
                                    )
                                )
                            )
                            .padding(22.dp)
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Color.White.copy(alpha = 0.22f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🍞",
                                    fontSize = 24.sp
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = business?.name ?: "Panadería",
                                color = Color.White,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = listOfNotNull(
                                    business?.city,
                                    business?.country
                                ).joinToString(", "),
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        // INFO TOP
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                        ) {

                            BreadInfoBox(
                                modifier = Modifier.weight(1f),
                                title = "Pedido",
                                value = "#${sale.sale.id}",
                                bg = beigeSoft,
                                text = text,
                                sub = sub
                            )

                            BreadInfoBox(
                                modifier = Modifier.weight(1f),
                                title = "Fecha",
                                value = formatSaleDate(sale.sale.created),
                                bg = beigeSoft,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        BreadInfoBox(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Método de Pago",
                            value = sale.sale.paymentMethod ?: "N/A",
                            bg = cream,
                            text = text,
                            sub = sub
                        )

                        if (!business?.phone.isNullOrBlank()) {

                            Spacer(Modifier.height(10.dp))

                            BreadInfoBox(
                                modifier = Modifier.fillMaxWidth(),
                                title = "Contacto",
                                value = business?.phone ?: "",
                                bg = cream,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(18.dp))

                        Text(
                            text = "PRODUCTOS",
                            fontSize = 11.sp,
                            color = sub,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(12.dp))

                        sale.items.forEach { item ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(cream)
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(beigeSoft)
                                        .padding(
                                            horizontal = 9.dp,
                                            vertical = 6.dp
                                        )
                                ) {
                                    Text(
                                        text = "${item.quantity}x",
                                        color = brown,
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
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(Modifier.height(2.dp))

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
                                    color = brownDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                            Spacer(Modifier.height(10.dp))
                        }

                        Spacer(Modifier.height(6.dp))

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
                                    text = "TOTAL",
                                    color = sub,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp
                                )

                                Text(
                                    text = "Gracias por visitarnos",
                                    color = sub,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = "$ ${
                                    "%.2f".format(sale.sale.amount)
                                }",
                                color = success,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp
                            )
                        }
                    }
                }
            }
    }
}

@Composable
private fun BreadInfoBox(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    bg: Color,
    text: Color,
    sub: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .padding(14.dp)
    ) {

        Text(
            text = title,
            color = sub,
            fontSize = 11.sp
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = value,
            color = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

