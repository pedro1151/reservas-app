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
fun ReciboMetal(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    // ⚙️ PALETA METAL / FERRETERÍA
    val bg = Color(0xFF111315)
    val card = Color(0xFF1A1D21)
    val cardSoft = Color(0xFF23272D)
    val border = Color(0xFF3C434D)

    val steel = Color(0xFFC0C7D1)
    val chrome = Color(0xFFE8EDF2)
    val graphite = Color(0xFF8B949E)

    val orange = Color(0xFFFF8C00)
    val yellow = Color(0xFFFFC107)
    val blue = Color(0xFF4FC3F7)

    val text = Color(0xFFF8FAFC)
    val sub = Color(0xFF9CA3AF)

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
                    steel.copy(alpha = 0.35f)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Column {

                    // ⚙️ HEADER METAL
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        graphite,
                                        steel,
                                        chrome
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
                                        Color.Black.copy(alpha = 0.14f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "⚙️",
                                    fontSize = 26.sp
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = business?.name
                                    ?: "Ferretería Metal",
                                color = Color(0xFF111315),
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
                                color = Color(0xFF2B2F34),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        // TOP DATA
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                        ) {

                            MetalInfoBox(
                                modifier = Modifier.weight(1f),
                                title = "FACTURA",
                                value = "#${sale.sale.id}",
                                bg = cardSoft,
                                line = orange,
                                text = text,
                                sub = sub
                            )

                            MetalInfoBox(
                                modifier = Modifier.weight(1f),
                                title = "FECHA",
                                value = formatSaleDate(
                                    sale.sale.created
                                ),
                                bg = cardSoft,
                                line = blue,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        MetalInfoBox(
                            modifier = Modifier.fillMaxWidth(),
                            title = "PAGO",
                            value = sale.sale.paymentMethod
                                ?: "N/A",
                            bg = cardSoft,
                            line = yellow,
                            text = text,
                            sub = sub
                        )

                        if (!business?.phone.isNullOrBlank()) {

                            Spacer(Modifier.height(10.dp))

                            MetalInfoBox(
                                modifier = Modifier.fillMaxWidth(),
                                title = "CONTACTO",
                                value = business?.phone ?: "",
                                bg = cardSoft,
                                line = steel,
                                text = text,
                                sub = sub
                            )
                        }

                        Spacer(Modifier.height(18.dp))

                        Text(
                            text = "MATERIALES",
                            color = orange,
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
                                            orange.copy(
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
                                        color = orange,
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
                                    color = steel,
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
                                    text = "TOTAL",
                                    color = sub,
                                    fontSize = 11.sp,
                                    letterSpacing = 1.sp
                                )

                                Text(
                                    text = "Calidad y resistencia",
                                    color = orange,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                text = "$ ${
                                    "%.2f".format(
                                        sale.sale.amount
                                    )
                                }",
                                color = yellow,
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
private fun MetalInfoBox(
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