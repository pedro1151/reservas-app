package com.optic.pramosreservasappz.presentation.screens.productos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.RoomService
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.presentation.ui.theme.BorderGray
import com.optic.pramosreservasappz.presentation.ui.theme.TextPrimary
import com.optic.pramosreservasappz.presentation.ui.theme.TextSecondary

@Composable
fun CatalogHeroStats(
    totalProducts: Int,
    totalValue: String
) {
    val hardcodedServices = 2
    val hardcodedProducts = 24

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(120.dp) // 🔥 NUEVO
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.24f)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            Color(0xFFD81B60)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp) // 🔥 leve ajuste
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.20f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Category,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Text(
                        text = "CATÁLOGO",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.78f),
                        letterSpacing = 1.1.sp
                    )
                }

                Spacer(Modifier.height(10.dp)) // 🔥 reducido

                Text(
                    text = "$totalProducts",
                    fontSize = 34.sp, // 🔥 leve ajuste para encajar
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-1.4).sp,
                    lineHeight = 34.sp
                )

                Text(
                    text = "items registrados",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.68f),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(120.dp) // 🔥 NUEVO
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(alpha = 0.03f),
                    spotColor = Color.Black.copy(alpha = 0.05f)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = BorderGray,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 14.dp, vertical = 12.dp) // 🔥 leve ajuste
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Inventory2,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Text(
                        text = "TIPOS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.1.sp
                    )
                }

                Spacer(Modifier.height(10.dp)) // 🔥 reducido

                CatalogTypeRow(
                    icon = Icons.Outlined.Inventory2,
                    label = "Productos",
                    value = hardcodedProducts
                )

                Spacer(Modifier.height(6.dp)) // 🔥 reducido

                CatalogTypeRow(
                    icon = Icons.Outlined.RoomService,
                    label = "Servicios",
                    value = hardcodedServices
                )
            }
        }
    }
}

@Composable
private fun CatalogTypeRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary.copy(alpha = 0.75f),
            modifier = Modifier.size(15.dp)
        )

        Spacer(Modifier.width(7.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "$value",
            fontSize = 15.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}