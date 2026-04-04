package com.optic.pramosreservasappz.presentation.screens.sales.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.presentation.screens.historial.components.getAvatarColor
import com.optic.pramosreservasappz.presentation.screens.historial.components.getInitials

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


    val avatarColor = remember(sale.id) { getAvatarColor(sale.id) }
    val initials = "#"+sale.id.toString()


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
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 AVATAR (columna 1)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                initials?.let {
                    Text(
                        text = it,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 🔹 INFO (columna 2)
            Column(
                modifier = Modifier.weight(1f)
            ) {

                // Descripción
                Text(
                    text = sale.description ?: "Venta",
                    fontSize = 14.sp,
                    fontWeight =  FontWeight.Normal,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                // Vendedor
                Row {
                    Text(
                        text = "Vendedor ",
                        fontSize = 12.sp,
                        color = SoftGray
                    )
                    Text(
                        text = "Fernando Suarez",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight =  FontWeight.Normal,
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Hora
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = SoftGray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 🔹 MONTO (columna 3 → extremo derecho)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "%.0f".format(sale.amount),
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

