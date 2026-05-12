package com.optic.pramozventicoappz.presentation.screens.inicio.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.sales.SaleResponse
import com.optic.pramozventicoappz.presentation.navigation.screen.client.ClientScreen
import com.optic.pramozventicoappz.presentation.screens.tusventas.components.getAvatarColor
import com.optic.pramozventicoappz.presentation.ui.theme.AccentText
import com.optic.pramozventicoappz.presentation.ui.theme.BadgeGrisBackground
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGrisSoftCard
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

@Composable
fun SaleCard(
    sale: SaleResponse,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val time = sale.created.substring(11, 16)

    val cardBg = MaterialTheme.colorScheme.surface
    val border = BorderGrisSoftCard
    val primary = MaterialTheme.colorScheme.primary

    val badgeBg = BadgeGrisBackground
    val accentSoft = Color(0xFFFFF6D8)
    val accentText = AccentText

    val avatarColor = remember(sale.id) { getAvatarColor(sale.id) }

    val saleCode = remember(sale.id) {
        "V-${sale.id.toString().padStart(5, '0')}"
    }


    val saleTitle = sale.description
        ?.takeIf { it.isNotBlank() }
        ?: "Venta rápida"

    val salesmanName = sale.salesman?.username
        ?.takeIf { it.isNotBlank() }
        ?: sale.salesman?.email
        ?: "Vendedor"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.035f),
                spotColor = primary.copy(alpha = 0.08f),
                clip = false
            )
            .clickable {
                navController.navigate(
                    ClientScreen.SaleDetail.createRoute(saleId = sale.id)
                )
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBg
        ),
        border = BorderStroke(
            width = 1.dp,
            color = border.copy(alpha = 0.88f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(accentSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (sale.paymentMethod == null) {
                        Icons.Outlined.Bolt
                    } else {
                       // Icons.Outlined.ReceiptLong
                        Icons.Outlined.Bolt
                    },
                    contentDescription = null,
                    tint = accentText,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = saleTitle,
                        color = TextPrimary,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = saleCode,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }

                sale.client?.let { client ->
                    Text(
                        text = client.fullName,
                        color = TextSecondary,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    SaleInfoBadge(
                        text = salesmanName,
                        bgColor = badgeBg,
                        textColor = TextSecondary,
                        icon = Icons.Outlined.Storefront
                    )

                    SaleInfoBadge(
                        text = time,
                        bgColor = badgeBg,
                        textColor = TextSecondary,
                        icon = Icons.Outlined.Schedule
                    )

                }
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = "$ ${"%.0f".format(sale.amount)}",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1
            )
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
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}