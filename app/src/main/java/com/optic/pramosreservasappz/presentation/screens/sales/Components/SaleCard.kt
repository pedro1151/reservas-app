package com.optic.pramosreservasappz.presentation.screens.sales.Components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.optic.pramosreservasappz.domain.model.sales.SaleResponse
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramosreservasappz.presentation.screens.historial.components.getAvatarColor
import com.optic.pramosreservasappz.presentation.screens.historial.components.getInitials
import com.optic.pramosreservasappz.presentation.screens.salestats.colors.Cyan

@Composable
fun SaleCard(
    sale: SaleResponse,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val time = sale.created.substring(11, 16)

    val TextPrimary = Color(0xFF0D0D0D)
    val TextSecondary = Color(0xFF6B7280)
    val Border = Color(0xFFE5E7EB)
    val Green = Color(0xFF16A34A)

    val avatarColor = remember(sale.id) { getAvatarColor(sale.id) }
    val initials = "#${sale.id}"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable {
                navController.navigate(
                    ClientScreen.SaleDetail.createRoute(saleId = sale.id)
                )
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                Text(initials, color = Color.White)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    sale.description ?: "Venta",
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    maxLines = 1
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Vendedor Fernando Suarez",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Text(
                    time,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Text(
                "$ ${"%.0f".format(sale.amount)}",
                fontWeight = FontWeight.SemiBold,
                color = Cyan,
                fontSize = 16.sp
            )
        }
    }
}
