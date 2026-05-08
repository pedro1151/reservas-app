package com.optic.pramozventicoappz.presentation.screens.recibos.tiposrecibos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun ReciboEstandar(
    sale: SaleWithItemsResponse,
    business: BusinessCompleteResponse?,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    val bg = Color(0xFFF8FAFC)
    val white = Color.White
    val border = Color(0xFFE5E7EB)
    val text = Color(0xFF0F172A)
    val sub = Color(0xFF64748B)
    val green = Color(0xFF16A34A)

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .padding(
                top = paddingValues.calculateTopPadding() + 14.dp,
                start = 14.dp,
                end = 14.dp,
                bottom = 100.dp
            ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            border = BorderStroke(1.dp, border),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = business?.name ?: "Mi Negocio",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = text,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = listOfNotNull(
                        business?.city,
                        business?.country
                    ).joinToString(", "),
                    fontSize = 13.sp,
                    color = sub
                )

                if (!business?.phone.isNullOrBlank()) {
                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = business?.phone ?: "",
                        fontSize = 13.sp,
                        color = sub
                    )
                }

                Spacer(Modifier.height(18.dp))

                Text(
                    text = "RECIBO DE VENTA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    color = sub
                )

                Spacer(Modifier.height(12.dp))

                Divider(color = border)

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                    Arrangement.SpaceBetween
                ) {
                    MiniLabel("N°")
                    MiniValue("#${sale.sale.id}")
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                    Arrangement.SpaceBetween
                ) {
                    MiniLabel("Fecha")
                    MiniValue(formatSaleDate(sale.sale.created))
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                    Arrangement.SpaceBetween
                ) {
                    MiniLabel("Pago")
                    MiniValue(
                        sale.sale.paymentMethod ?: "N/A"
                    )
                }

                Spacer(Modifier.height(16.dp))

                Divider(color = border)

                Spacer(Modifier.height(14.dp))

                sale.items.forEach { item ->

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = item.product?.name
                                    ?: "Producto",
                                fontSize = 14.sp,
                                color = text,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow =
                                TextOverflow.Ellipsis
                            )

                            Text(
                                text = "$ ${
                                    "%.2f".format(
                                        item.price *
                                                item.quantity
                                    )
                                }",
                                fontSize = 14.sp,
                                color = text,
                                fontWeight =
                                FontWeight.SemiBold
                            )
                        }

                        Spacer(Modifier.height(2.dp))

                        Text(
                            text = "${item.quantity} x $ ${
                                "%.2f".format(item.price)
                            }",
                            fontSize = 12.sp,
                            color = sub
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                }

                Divider(color = border)

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                    Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "TOTAL",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = text
                    )

                    Text(
                        text = "$ ${
                            "%.2f".format(
                                sale.sale.amount
                            )
                        }",
                        fontSize = 24.sp,
                        fontWeight =
                        FontWeight.ExtraBold,
                        color = green
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Gracias por su compra",
                    fontSize = 12.sp,
                    color = sub
                )
            }
        }
    }
}

@Composable
private fun MiniLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = Color(0xFF64748B)
    )
}

@Composable
private fun MiniValue(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = Color(0xFF0F172A),
        fontWeight = FontWeight.SemiBold
    )
}