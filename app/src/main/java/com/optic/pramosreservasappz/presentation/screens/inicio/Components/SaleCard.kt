// SaleCard.kt
package com.optic.pramosreservasappz.presentation.screens.inicio.Components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.tusventas.components.getAvatarColor

@Composable
fun SaleCard(
    sale: SaleResponse,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val time = sale.created.substring(11, 16)

    val CardBg = Color.White
    val Border = Color(0xFFF3D6E2)
    val Title = Color(0xFF111827)
    val Subtitle = Color(0xFF6B7280)
    val Amount = Color(0xFFE91E63)

    val avatarColor = remember(sale.id) { getAvatarColor(sale.id) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable {
                navController.navigate(
                    ClientScreen.SaleDetail.createRoute(saleId = sale.id)
                )
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = BorderStroke(1.dp, Border),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "#${sale.id}",
                    color = Color.White
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = sale.description ?: "Venta",
                    color = Title
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Vendedor Fernando Suarez",
                    color = Subtitle,
                    fontSize = 12.sp
                )

                Text(
                    text = time,
                    color = Subtitle,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "$ ${"%.0f".format(sale.amount)}",
                color = Amount,
                fontSize = 16.sp
            )
        }
    }
}