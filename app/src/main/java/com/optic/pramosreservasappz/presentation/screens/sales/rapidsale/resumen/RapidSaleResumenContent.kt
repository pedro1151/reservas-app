package com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.resumen

import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.presentation.ui.theme.GrisModerno

@Composable
fun RapidSaleResumenContent(
    selectedProducts: List<Pair<ProductResponse, Int>>,
    paddingValues: PaddingValues,
    total: Double,
    totalItems: Int
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFF6F7FB)), // 🔥 gris moderno suave
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {

        // 🔹 HEADER
        item {
            Text(
                text = "Resumen",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "$totalItems productos",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(Modifier.height(18.dp))
        }

        // 🔹 ITEMS
        items(selectedProducts) { (product, quantity) ->

            val subtotal = product.price * quantity

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🔥 BADGE LATAM (más suave)
                Box(
                    modifier = Modifier
                        .height(35.dp)
                        .defaultMinSize(minWidth = 35.dp)
                        .background(
                            color = Color(0xFF6A5AE0).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = quantity.toString(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6A5AE0),
                            fontSize = 16.sp
                        )

                        Text(
                            text = "x",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6A5AE0),
                            fontSize = 14.sp, // 🔥 un pelín más chico queda más pro
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                // 🔹 INFO
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = Color(0xFF111827)
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "$ ${product.price}",
                        fontSize = 16.sp,
                        color = Color(0xFF9CA3AF)
                    )
                    // 🔥 línea suave
                    Spacer(Modifier.height(5.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color(0xFFF1F1F1))
                    )
                }

                // 🔥 SUBTOTAL (más pro)
                Text(
                    text = "$ ${"%.2f".format(subtotal)}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = Color(0xFF111827)
                )
            }
        }

        // 🔹 SEPARADOR SUAVE (aire, no línea dura)
        item {
            Spacer(Modifier.height(16.dp))
        }

        // 🔥 TOTAL BLOCK (Stripe + Kyte)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE5E7EB),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(18.dp)
            ) {



                Spacer(Modifier.height(12.dp))



                Spacer(Modifier.height(12.dp))

                // 🔥 TOTAL FINAL (pro)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        "TOTAL",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Text(
                        "$ ${"%.2f".format(total)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GrisModerno
                    )
                }
            }
        }

        // 🔥 espacio para botón
        item {
            Spacer(Modifier.height(90.dp))
        }
    }
}