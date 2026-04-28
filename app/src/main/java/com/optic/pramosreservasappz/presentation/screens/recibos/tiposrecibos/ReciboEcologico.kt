package com.optic.pramosreservasappz.presentation.screens.recibos.tiposrecibos


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
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramosreservasappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramosreservasappz.presentation.util.formatSaleDate

@Composable
fun ReciboEcologico(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    // 🌿 PALETA ECO / PLANTAS
    val bg = Color(0xFFF4FAF4)
    val card = Color.White
    val cardSoft = Color(0xFFF7FCF7)
    val border = Color(0xFFD6E8D6)

    val green = Color(0xFF2E7D32)
    val greenSoft = Color(0xFF43A047)
    val lime = Color(0xFF8BC34A)
    val forest = Color(0xFF1B5E20)
    val mint = Color(0xFFE8F5E9)

    val text = Color(0xFF1F2937)
    val sub = Color(0xFF6B7280)

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
                colors = CardDefaults.cardColors(
                    containerColor = card
                ),
                border = BorderStroke(1.dp, border),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Column {

                    // 🌿 HEADER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        forest,
                                        green,
                                        lime
                                    )
                                )
                            )
                            .padding(22.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(74.dp)
                                .offset(x = (-14).dp, y = (-14).dp)
                                .clip(CircleShape)
                                .background(
                                    Color.White.copy(alpha = 0.08f)
                                )
                        )

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 8.dp, y = (-8).dp)
                                .clip(CircleShape)
                                .background(
                                    Color.White.copy(alpha = 0.10f)
                                )
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment =
                            Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Color.White.copy(alpha = 0.16f)
                                    ),
                                contentAlignment =
                                Alignment.Center
                            ) {
                                Text(
                                    text = "🌿",
                                    fontSize = 28.sp
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = business?.name
                                    ?: "Eco Store",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight =
                                FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = listOfNotNull(
                                    business?.city,
                                    business?.country
                                ).joinToString(", "),
                                color = Color.White.copy(
                                    alpha = 0.90f
                                ),
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

                            EcoInfoBox(
                                modifier = Modifier.weight(1f),
                                title = "Venta",
                                value = "#${sale.sale.id}",
                                bg = mint,
                                line = green,
                                text = text,
                                sub = sub
                            )

                            EcoInfoBox(
                                modifier = Modifier.weight(1f),
                                title = "Fecha",
                                value = formatSaleDate(
                                    sale.sale.created
                                ),
                                bg = mint,
                                line = lime,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        EcoInfoBox(
                            modifier = Modifier.fillMaxWidth(),
                            title = "Pago",
                            value = sale.sale.paymentMethod
                                ?: "N/A",
                            bg = cardSoft,
                            line = greenSoft,
                            text = text,
                            sub = sub
                        )

                        if (!business?.phone.isNullOrBlank()) {

                            Spacer(Modifier.height(10.dp))

                            EcoInfoBox(
                                modifier = Modifier.fillMaxWidth(),
                                title = "Contacto",
                                value = business?.phone ?: "",
                                bg = cardSoft,
                                line = forest,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(18.dp))

                        Text(
                            text = "PRODUCTOS",
                            color = green,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        // 🌿 ITEMS
                        sale.items.forEach { item ->

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(
                                        RoundedCornerShape(22.dp)
                                    )
                                    .background(cardSoft)
                            ) {

                                // círculos internos decorativos
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .offset(
                                            x = (-18).dp,
                                            y = (-18).dp
                                        )
                                        .clip(CircleShape)
                                        .background(
                                            lime.copy(
                                                alpha = 0.07f
                                            )
                                        )
                                )

                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .align(
                                            Alignment.BottomEnd
                                        )
                                        .offset(
                                            x = 10.dp,
                                            y = 10.dp
                                        )
                                        .clip(CircleShape)
                                        .background(
                                            green.copy(
                                                alpha = 0.06f
                                            )
                                        )
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment =
                                    Alignment.CenterVertically
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(
                                                CircleShape
                                            )
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(
                                                        green,
                                                        lime
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
                                            FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }

                                    Spacer(
                                        Modifier.width(12.dp)
                                    )

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = item.product?.name
                                                ?: "Producto",
                                            color = text,
                                            fontWeight =
                                            FontWeight.Bold,
                                            fontSize = 14.sp,
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
                                        color = forest,
                                        fontWeight =
                                        FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                }
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
                                    text = "Gracias por cuidar el planeta",
                                    color = green,
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
                                color = forest,
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
private fun EcoInfoBox(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    bg: Color,
    line: Color,
    text: Color,
    sub: Color
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .padding(14.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(line)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = title,
            color = sub,
            fontSize = 11.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = value,
            color = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}