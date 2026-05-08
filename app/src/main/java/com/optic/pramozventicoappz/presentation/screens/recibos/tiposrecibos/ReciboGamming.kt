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
fun ReciboGamming(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    // 🎮 PALETA GAMING / TECH
    val bg = Color(0xFF06070D)
    val card = Color(0xFF10131C)
    val cardSoft = Color(0xFF161A26)
    val border = Color(0xFF273043)

    val neonBlue = Color(0xFF00E5FF)
    val neonGreen = Color(0xFF39FF14)
    val neonPurple = Color(0xFFB026FF)
    val neonPink = Color(0xFFFF2DA6)

    val text = Color(0xFFF8FAFC)
    val sub = Color(0xFF94A3B8)

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
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = card),
                border = BorderStroke(
                    1.dp,
                    neonBlue.copy(alpha = 0.45f)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Column {

                    // 🎮 HEADER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        neonPurple,
                                        neonBlue,
                                        neonGreen
                                    )
                                )
                            )
                            .padding(22.dp)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Color.White.copy(alpha = 0.18f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🎮",
                                    fontSize = 26.sp
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = business?.name
                                    ?: "Gaming Store",
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
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        // TOP STATUS
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                        ) {

                            GamingStatBox(
                                modifier = Modifier.weight(1f),
                                title = "ORDER",
                                value = "#${sale.sale.id}",
                                bg = cardSoft,
                                line = neonBlue,
                                text = text,
                                sub = sub
                            )

                            GamingStatBox(
                                modifier = Modifier.weight(1f),
                                title = "DATE",
                                value = formatSaleDate(
                                    sale.sale.created
                                ),
                                bg = cardSoft,
                                line = neonGreen,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        GamingStatBox(
                            modifier = Modifier.fillMaxWidth(),
                            title = "PAYMENT",
                            value = sale.sale.paymentMethod
                                ?: "N/A",
                            bg = cardSoft,
                            line = neonPink,
                            text = text,
                            sub = sub
                        )

                        if (!business?.phone.isNullOrBlank()) {

                            Spacer(Modifier.height(10.dp))

                            GamingStatBox(
                                modifier = Modifier.fillMaxWidth(),
                                title = "SUPPORT",
                                value = business?.phone ?: "",
                                bg = cardSoft,
                                line = neonPurple,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(18.dp))

                        Text(
                            text = "ITEMS",
                            color = neonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        // 🎮 ITEMS
                        sale.items.forEach { item ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(
                                        RoundedCornerShape(18.dp)
                                    )
                                    .background(cardSoft)
                                    .padding(14.dp),
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
                                            neonBlue.copy(
                                                alpha = 0.14f
                                            )
                                        )
                                        .padding(
                                            horizontal = 10.dp,
                                            vertical = 6.dp
                                        )
                                ) {
                                    Text(
                                        text = "${item.quantity}x",
                                        color = neonBlue,
                                        fontWeight =
                                        FontWeight.Bold,
                                        fontSize = 12.sp
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
                                        FontWeight.SemiBold,
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
                                    color = neonGreen,
                                    fontWeight =
                                    FontWeight.Bold,
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
                                    text = "TOTAL XP",
                                    color = sub,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp
                                )

                                Text(
                                    text = "GG • Thanks!",
                                    color = neonBlue,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = "$ ${
                                    "%.2f".format(
                                        sale.sale.amount
                                    )
                                }",
                                color = neonGreen,
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
private fun GamingStatBox(
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
                .clip(RoundedCornerShape(10.dp))
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