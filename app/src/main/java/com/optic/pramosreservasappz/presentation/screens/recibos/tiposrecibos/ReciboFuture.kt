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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.domain.model.business.completebusiness.BusinessCompleteResponse
import com.optic.pramosreservasappz.domain.model.sales.SaleWithItemsResponse
import com.optic.pramosreservasappz.presentation.util.formatSaleDate

@Composable
fun ReciboFuture(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    // 🚀 FUTURISTA
    val bg = Color(0xFF050816)
    val card = Color(0xFF0D1326)
    val cardSoft = Color(0xFF121A32)
    val border = Color(0xFF233259)

    val cyan = Color(0xFF00E5FF)
    val violet = Color(0xFF7C4DFF)
    val blue = Color(0xFF2979FF)
    val green = Color(0xFF00E676)

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
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = card
                ),
                border = BorderStroke(
                    1.dp,
                    cyan.copy(alpha = 0.35f)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Column {

                    // 🚀 HEADER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        violet,
                                        blue,
                                        cyan
                                    )
                                )
                            )
                            .padding(22.dp)
                    ) {

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
                                        Color.White.copy(
                                            alpha = 0.18f
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "◉",
                                    color = Color.White,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = business?.name
                                    ?: "Future Store",
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

                        // STATUS BOXES
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                        ) {

                            FutureBox(
                                modifier = Modifier.weight(1f),
                                title = "ID",
                                value = "#${sale.sale.id}",
                                line = cyan,
                                bg = cardSoft,
                                text = text,
                                sub = sub
                            )

                            FutureBox(
                                modifier = Modifier.weight(1f),
                                title = "DATE",
                                value = formatSaleDate(
                                    sale.sale.created
                                ),
                                line = violet,
                                bg = cardSoft,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        FutureBox(
                            modifier = Modifier.fillMaxWidth(),
                            title = "PAYMENT",
                            value = sale.sale.paymentMethod
                                ?: "N/A",
                            line = blue,
                            bg = cardSoft,
                            text = text,
                            sub = sub
                        )

                        if (!business?.phone.isNullOrBlank()) {

                            Spacer(Modifier.height(10.dp))

                            FutureBox(
                                modifier = Modifier.fillMaxWidth(),
                                title = "CONTACT",
                                value = business?.phone ?: "",
                                line = green,
                                bg = cardSoft,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(18.dp))

                        Text(
                            text = "ITEM MATRIX",
                            color = cyan,
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
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(cardSoft)
                                    .drawBehind {

                                        drawCircle(
                                            color = cyan.copy(
                                                alpha = 0.05f
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
                                            color = violet.copy(
                                                alpha = 0.05f
                                            ),
                                            radius = 80f,
                                            center =
                                            center.copy(
                                                x = size.width * 0.85f,
                                                y = size.height * 0.5f
                                            ),
                                            style = Fill
                                        )
                                    }
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
                                            cyan.copy(
                                                alpha = 0.12f
                                            )
                                        )
                                        .padding(
                                            horizontal = 10.dp,
                                            vertical = 6.dp
                                        )
                                ) {
                                    Text(
                                        text = "${item.quantity}x",
                                        color = cyan,
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
                                        fontWeight =
                                        FontWeight.SemiBold,
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
                                    color = green,
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
                                    text = "TOTAL VALUE",
                                    color = sub,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp
                                )

                                Text(
                                    text = "Thanks for evolving",
                                    color = cyan,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = "$ ${
                                    "%.2f".format(
                                        sale.sale.amount
                                    )
                                }",
                                color = green,
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
private fun FutureBox(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    line: Color,
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