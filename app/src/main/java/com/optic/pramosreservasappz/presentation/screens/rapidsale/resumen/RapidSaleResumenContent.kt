package com.optic.pramosreservasappz.presentation.screens.rapidsale.resumen

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
import androidx.compose.ui.draw.shadow
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

    val Cyan = Color(0xFF22C1C3)
    val CyanSoft = Color(0xFF22C1C3).copy(alpha = 0.12f)

    val TextPrimary = Color(0xFF0F172A)
    val TextSecondary = Color(0xFF475569)
    val BorderSoft = Color(0xFFE2E8F0)
    val BackgroundSoft = Color(0xFFF8FAFC)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(BackgroundSoft),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {

        // 🔹 HEADER
        item {
            Text(
                text = "Resumen",
                fontSize = 22.sp, // 🔥 más jerarquía
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "$totalItems productos",
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(Modifier.height(18.dp))
        }

        // 🔹 ITEMS
        items(selectedProducts) { (product, quantity) ->

            val subtotal = product.price * quantity

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black.copy(alpha = 0.05f),
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🔥 NUEVO BADGE (MUCHO mejor)
                Box(
                    modifier = Modifier
                        .background(
                            color = CyanSoft,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$quantity x",
                        fontWeight = FontWeight.Bold,
                        color = Cyan,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "$ ${product.price}",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }

                // 🔥 SUBTOTAL destacado
                Text(
                    text = "$ ${"%.2f".format(subtotal)}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
            }

            // 🔥 DIVIDER MODERNO (no bloque pesado)
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderSoft)
            )
        }

        item { Spacer(Modifier.height(18.dp)) }

        // 🔥 TOTAL BLOCK (más vivo)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color.Black.copy(alpha = 0.05f),
                            spotColor = Color.Black.copy(alpha = 0.08f)
                        )
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "Total",
                        fontSize = 16.sp,
                        color = TextSecondary
                    )

                    Text(
                        "$ ${"%.2f".format(total)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Cyan
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(90.dp))
        }
    }

}