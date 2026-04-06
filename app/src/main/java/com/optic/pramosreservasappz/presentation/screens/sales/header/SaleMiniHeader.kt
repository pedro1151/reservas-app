package com.optic.pramosreservasappz.presentation.screens.sales.header



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.pramosreservasappz.presentation.navigation.screen.client.ClientScreen
@Composable
fun SaleMiniHeader(
    todayTotal: Double,
    balanceHidden: Boolean,
    navController: NavHostController,
    onToggleHide:   () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // 🔥 IZQUIERDA → HOY + MONTO
        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = "HOY",
                fontSize = 12.sp,
                fontWeight = FontWeight.W600,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = if (balanceHidden) "$••••" else "$${"%.0f".format(todayTotal)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // 🔥 DERECHA → BOTÓN PEQUEÑO
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF0F172A))
                .clickable {
                    navController.navigate(ClientScreen.RapidSale.route)
                }
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Bolt,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    "Venta",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }
}