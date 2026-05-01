package com.optic.pramosreservasappz.presentation.screens.inicio.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
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

    val cardBg = Color.White
    val border = Color(0xFFF1F5F9)
    val title = Color(0xFF0F172A)
    val subtitle = Color(0xFF64748B)
    val amount = Color(0xFFE91E63)

    val badgeBg = Color(0xFFF8FAFC)
    val badgeText = subtitle

    val avatarColor = remember(sale.id) { getAvatarColor(sale.id) }

    val salesmanName = sale.salesman?.username
        ?.takeIf { it.isNotBlank() }
        ?: sale.salesman?.email
        ?: "Vendedor"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp) // 🔥 ALTURA FIJA
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable {
                navController.navigate(
                    ClientScreen.SaleDetail.createRoute(saleId = sale.id)
                )
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, border),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ── Avatar ─────────────────────────────
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#${sale.id}",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            // ── Contenido ───────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // 🔥 Título
                Text(
                    text = sale.description ?: "Venta rápida",
                    color = title,
                    fontSize = 14.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 🔥 Metadata
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SaleInfoBadge(
                        text = salesmanName,
                        bgColor = badgeBg,
                        textColor = badgeText,
                        icon = Icons.Outlined.Storefront
                    )

                    SaleInfoBadge(
                        text = time,
                        bgColor = badgeBg,
                        textColor = badgeText,
                        icon = Icons.Outlined.Schedule
                    )
                }

                // 🔥 Cliente
                sale.client?.let { client ->
                    Text(
                        text = client.fullName,
                        color = subtitle,
                        fontSize = 11.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            // ── Monto ─────────────────────────────
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$ ${"%.0f".format(sale.amount)}",
                    color = amount,
                    fontSize = 16.5.sp
                )
            }
        }
    }
}

@Composable
private fun SaleInfoBadge(
    text: String,
    bgColor: Color,
    textColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bgColor)
            .padding(horizontal = 7.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(12.dp)
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = text,
            color = textColor,
            fontSize = 10.5.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}