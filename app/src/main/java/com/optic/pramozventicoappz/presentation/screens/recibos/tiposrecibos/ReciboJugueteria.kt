package com.optic.pramozventicoappz.presentation.screens.recibos.tiposrecibos


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@Composable
fun ReciboJugueteria(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    // 🎈 PALETA JUGUETERÍA
    val bg = Color(0xFFF7FBFF)
    val card = Color.White
    val cardSoft = Color(0xFFF1F8FF)
    val border = Color(0xFFD6E9FF)

    val blue = Color(0xFF2196F3)
    val green = Color(0xFF00C853)
    val yellow = Color(0xFFFFC107)
    val red = Color(0xFFFF5252)
    val purple = Color(0xFF7C4DFF)

    val text = Color(0xFF1E293B)
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



            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = card),
                border = BorderStroke(1.dp, border),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Column {

                    // 🎈 HEADER COLORIDO
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        blue,
                                        green,
                                        yellow,
                                        red
                                    )
                                )
                            )
                            .padding(22.dp)
                    ) {

                        // círculos decorativos
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .offset(x = (-10).dp, y = (-10).dp)
                                .clip(CircleShape)
                                .background(
                                    Color.White.copy(alpha = 0.16f)
                                )
                        )

                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 8.dp, y = (-8).dp)
                                .clip(CircleShape)
                                .background(
                                    Color.White.copy(alpha = 0.18f)
                                )
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Color.White.copy(alpha = 0.20f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🧸",
                                    fontSize = 28.sp
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = business?.name
                                    ?: "Juguetería",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = listOfNotNull(
                                    business?.city,
                                    business?.country
                                ).joinToString(", "),
                                color = Color.White.copy(alpha = 0.92f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        // INFO SUPERIOR
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                        ) {

                            ToyInfoChip(
                                modifier = Modifier.weight(1f),
                                title = "Venta",
                                value = "#${sale.sale.id}",
                                bg = blue.copy(alpha = 0.10f),
                                dot = blue,
                                text = text,
                                sub = sub
                            )

                            ToyInfoChip(
                                modifier = Modifier.weight(1f),
                                title = "Fecha",
                                value = formatSaleDate(
                                    sale.sale.created
                                ),
                                bg = green.copy(alpha = 0.10f),
                                dot = green,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        ToyInfoChip(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Pago",
                            value = sale.sale.paymentMethod
                                ?: "N/A",
                            bg = yellow.copy(alpha = 0.12f),
                            dot = yellow,
                            text = text,
                            sub = sub
                        )

                        if (!business?.phone.isNullOrBlank()) {

                            Spacer(Modifier.height(10.dp))

                            ToyInfoChip(
                                modifier = Modifier.fillMaxWidth(),
                                title = "Contacto",
                                value = business?.phone ?: "",
                                bg = purple.copy(alpha = 0.10f),
                                dot = purple,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(18.dp))

                        Text(
                            text = "JUGUETES",
                            color = red,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        // ITEMS
                        sale.items.forEach { item ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(
                                        RoundedCornerShape(22.dp)
                                    )
                                    .background(cardSoft)
                                    .padding(14.dp),
                                verticalAlignment =
                                Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                listOf(
                                                    blue,
                                                    green
                                                )
                                            )
                                        ),
                                    contentAlignment =
                                    Alignment.Center
                                ) {
                                    Text(
                                        text = "${item.quantity}",
                                        color = Color.White,
                                        fontWeight =
                                        FontWeight.ExtraBold,
                                        fontSize = 13.sp
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = item.product?.name
                                            ?: "Producto",
                                        color = text,
                                        fontSize = 14.sp,
                                        fontWeight =
                                        FontWeight.Bold,
                                        maxLines = 1,
                                        overflow =
                                        TextOverflow.Ellipsis
                                    )

                                    Spacer(
                                        Modifier.height(2.dp)
                                    )

                                    Text(
                                        text = "$ ${
                                            "%.2f".format(
                                                item.price
                                            )
                                        } c/u",
                                        color = sub,
                                        fontSize = 12.sp
                                    )
                                }

                                Text(
                                    text = "$ ${
                                        "%.2f".format(
                                            item.price *
                                                    item.quantity
                                        )
                                    }",
                                    color = green,
                                    fontWeight =
                                    FontWeight.ExtraBold,
                                    fontSize = 15.sp
                                )
                            }

                            Spacer(Modifier.height(10.dp))
                        }

                        Spacer(Modifier.height(8.dp))

                        Divider(
                            color = border,
                            thickness = 1.dp
                        )

                        Spacer(Modifier.height(16.dp))

                        // TOTAL
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment =
                            Alignment.CenterVertically
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
                                    text = "¡Gracias por jugar!",
                                    color = blue,
                                    fontSize = 12.sp,
                                    fontWeight =
                                    FontWeight.SemiBold
                                )
                            }

                            Text(
                                text = "$ ${
                                    "%.2f".format(
                                        sale.sale.amount
                                    )
                                }",
                                color = red,
                                fontSize = 30.sp,
                                fontWeight =
                                FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

    }
}

@Composable
private fun ToyInfoChip(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    bg: Color,
    dot: Color,
    text: Color,
    sub: Color
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .padding(14.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dot)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = title,
                color = sub,
                fontSize = 11.sp
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = value,
            color = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}