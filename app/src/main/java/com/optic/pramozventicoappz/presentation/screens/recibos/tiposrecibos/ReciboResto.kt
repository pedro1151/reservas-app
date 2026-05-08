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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramozventicoappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramozventicoappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramozventicoappz.presentation.util.formatSaleDate

@Composable
fun ReciboResto(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    // 🍷 PALETA RESTAURANT
    val bg = Color(0xFFF8F3F4)

    val card = Color(0xFFFFFCFC)
    val cardSoft = Color(0xFFF7ECEE)

    val wine = Color(0xFF7B1E3A)
    val wineDark = Color(0xFF5A1329)
    val burgundy = Color(0xFF9E2746)

    val gold = Color(0xFFD4AF37)
    val goldSoft = Color(0xFFF4E5A6)

    val border = Color(0xFFE5D2D7)

    val text = Color(0xFF111111)
    val sub = Color(0xFF5F5F5F)
    val success = Color(0xFF222222)

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

                    // HEADER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        wineDark,
                                        wine,
                                        burgundy
                                    )
                                )
                            )
                            .padding(
                                horizontal = 20.dp,
                                vertical = 18.dp
                            )
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment =
                            Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(
                                        goldSoft.copy(
                                            alpha = 0.28f
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🍷",
                                    fontSize = 24.sp
                                )
                            }

                            Spacer(Modifier.width(14.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = business?.name
                                        ?: "Restaurant",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight =
                                    FontWeight.Bold
                                )

                                Spacer(
                                    Modifier.height(2.dp)
                                )

                                Text(
                                    text = listOfNotNull(
                                        business?.city,
                                        business?.country
                                    ).joinToString(", "),
                                    color = Color.White.copy(
                                        alpha = 0.85f
                                    ),
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = "#${sale.sale.id}",
                                color = gold,
                                fontSize = 13.sp,
                                fontWeight =
                                FontWeight.ExtraBold
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        // TOP INFO
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                        ) {

                            RestaurantInfo(
                                modifier = Modifier.weight(1f),
                                title = "Fecha",
                                value = formatSaleDate(
                                    sale.sale.created
                                ),
                                bg = cardSoft,
                                titleColor = sub,
                                valueColor = text
                            )

                            RestaurantInfo(
                                modifier = Modifier.weight(1f),
                                title = "Pago",
                                value = sale.sale.paymentMethod
                                    ?: "N/A",
                                bg = cardSoft,
                                titleColor = sub,
                                valueColor = text
                            )
                        }

                        if (!business?.phone.isNullOrBlank()) {

                            Spacer(Modifier.height(8.dp))

                            RestaurantInfo(
                                modifier = Modifier.fillMaxWidth(),
                                title = "Reserva / Contacto",
                                value = business?.phone ?: "",
                                bg = cardSoft,
                                titleColor = sub,
                                valueColor = text
                            )
                        }

                        Spacer(Modifier.height(14.dp))

                        Text(
                            text = "DETALLE",
                            color = wine,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(Modifier.height(8.dp))

                        // ITEMS
                        sale.items.forEach { item ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(
                                        RoundedCornerShape(
                                            18.dp
                                        )
                                    )
                                    .background(cardSoft)
                                    .drawBehind {

                                        drawCircle(
                                            color = wine.copy(
                                                alpha = 0.035f
                                            ),
                                            radius = 90f,
                                            center =
                                            center.copy(
                                                x = size.width * 0.15f,
                                                y = size.height * 0.5f
                                            ),
                                            style = Fill
                                        )

                                        drawCircle(
                                            color = gold.copy(
                                                alpha = 0.04f
                                            ),
                                            radius = 70f,
                                            center =
                                            center.copy(
                                                x = size.width * 0.90f,
                                                y = size.height * 0.5f
                                            ),
                                            style = Fill
                                        )
                                    }
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 9.dp
                                    ),
                                verticalAlignment =
                                Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                12.dp
                                            )
                                        )
                                        .background(
                                            wine.copy(
                                                alpha = 0.09f
                                            )
                                        )
                                        .padding(
                                            horizontal = 8.dp,
                                            vertical = 5.dp
                                        )
                                ) {
                                    Text(
                                        text = "${item.quantity}x",
                                        color = wine,
                                        fontSize = 11.sp,
                                        fontWeight =
                                        FontWeight.Bold
                                    )
                                }

                                Spacer(Modifier.width(10.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = item.product?.name
                                            ?: "Producto",
                                        color = text,
                                        fontSize = 13.sp,
                                        fontWeight =
                                        FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow =
                                        TextOverflow.Ellipsis
                                    )

                                    Spacer(
                                        Modifier.height(1.dp)
                                    )

                                    Text(
                                        text = "$ ${
                                            "%.2f".format(
                                                item.price
                                            )
                                        } c/u",
                                        color = sub,
                                        fontSize = 11.sp
                                    )
                                }

                                Text(
                                    text = "$ ${
                                        "%.2f".format(
                                            item.price *
                                                    item.quantity
                                        )
                                    }",
                                    color = text,
                                    fontSize = 14.sp,
                                    fontWeight =
                                    FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.height(7.dp))
                        }

                        Spacer(Modifier.height(6.dp))

                        Divider(color = border)

                        Spacer(Modifier.height(14.dp))

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
                                    text = "Gracias por visitarnos",
                                    color = wine,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = "$ ${
                                    "%.2f".format(
                                        sale.sale.amount
                                    )
                                }",
                                color = success,
                                fontSize = 28.sp,
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
private fun RestaurantInfo(
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
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            )
    ) {

        Text(
            text = title,
            color = titleColor,
            fontSize = 10.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = value,
            color = valueColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}