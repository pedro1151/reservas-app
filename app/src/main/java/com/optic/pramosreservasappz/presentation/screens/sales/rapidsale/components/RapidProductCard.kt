package com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.optic.pramosreservasappz.domain.model.product.ProductResponse
import com.optic.pramosreservasappz.presentation.sales.Components.SBlack
import com.optic.pramosreservasappz.presentation.sales.Components.SGray600
import com.optic.pramosreservasappz.presentation.ui.theme.BlueGradient
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.GreenGradient


@Composable
fun RapidProductCard(
    product: ProductResponse,
    addProduct: () -> Unit,
    removeProduct: () -> Unit,
    inCart: Pair<ProductResponse, Int>?,
    modifier: Modifier
) {

    val gradientRed = Brush.horizontalGradient(
        listOf(
            Color(0xFFEF5350),
            Color(0xFFEF5350)
        )
    )


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                addProduct()
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderGray),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp // 🔥 casi flat (más elegante)
        )
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // 🔹 IZQUIERDA → nombre + precio
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🔹 ICONO (extremo izquierdo)
                Icon(
                    imageVector = Icons.Default.CheckCircleOutline,
                    contentDescription = null,
                    tint = if (inCart != null) Color(0xFF22C55E) else Color(
                        0xFFE5E7EB
                    ),
                    modifier = Modifier.size(22.dp)
                )

                Spacer(Modifier.width(12.dp))

                // 🔹 TEXTO (nombre + precio)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        product.name,
                        fontWeight = FontWeight.Medium,
                        color = SBlack
                    )

                    Text(
                        "$ ${product.price}",
                        color = SGray600,
                        fontSize = 13.sp
                    )
                }

                // 🔹 CONTROLES (derecha)

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (inCart == null) {

                        // 🟢 SOLO BOTÓN +
                        SmallCircleButton(
                            text = "+",
                            backgroundColor = GreenGradient,
                            onClick = { addProduct() }
                        )

                    } else {

                        // 🔵 CONTROLES COMPLETOS
                        SmallCircleButton(
                            text = "-",
                            backgroundColor = gradientRed,
                            onClick = { removeProduct() }
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(
                            "${inCart.second}",
                            fontWeight = FontWeight.Bold,
                            color = SBlack
                        )

                        Spacer(Modifier.width(8.dp))

                        SmallCircleButton(
                            text = "+",
                            backgroundColor = GreenGradient,
                            onClick = { addProduct() }
                        )
                    }


                }
            }
        }

            // 🔹 DERECHA → botones (+ / -)
    }

}