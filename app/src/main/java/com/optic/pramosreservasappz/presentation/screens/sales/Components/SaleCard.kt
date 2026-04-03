package com.optic.pramosreservasappz.presentation.screens.sales.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.optic.pramosreservasappz.domain.model.sales.SaleResponse

@Composable
fun SaleCard(
    sale: SaleResponse,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val time = sale.created.substring(11, 16)

    val Violet = Color(0xFF7C3AED)
    val SoftGray = Color(0xFF6B7280)
    val BorderGray = Color(0xFFE5E7EB)
    val TextPrimary = Color(0xFF111111)
    val SalesmanGreen = Color(0xFF059669)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .then(
                if (onClick != null) Modifier.clickable { onClick() }
                else Modifier
            ),
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
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                // 🔹 DESCRIPCIÓN
                Text(
                    text = sale.description ?: "Venta",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 🔹 META INFO (hora + vendedor)


                // 🔹 META INFO (hora + vendedor)
                Row {


                    Text(
                        text = "Vendedor",
                        fontSize = 13.sp,
                        color = SoftGray,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Fernando Suarez",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        fontWeight = FontWeight.Medium
                    )

                }
                Row {
                    Text(
                        text = time,
                        fontSize = 13.sp,
                        color = SoftGray
                    )

                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 🔹 MONTO (PRO STYLE)
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$",
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "%.0f".format(sale.amount),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

