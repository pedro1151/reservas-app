package com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.topbar

import androidx.compose.ui.geometry.Offset
import com.optic.pramosreservasappz.presentation.screens.sales.rapidsale.components.MiniCart


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.optic.pramosreservasappz.presentation.ui.theme.GradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RapidSaleTopBar(
    title: String = "VENTA RAPIDA",
    navController: NavController,
    showTitle: Boolean = true,
    total: Double,
    totalItems: Int,
    onPositioned: (Offset) -> Unit,
    modifier: Modifier
) {



    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(GradientBackground)
            .drawBehind {
                drawRect(
                    color = Color.Black.copy(alpha = 0.05f)
                )
            }
    ) {
        TopAppBar(
            title = {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )

            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        // tint = MaterialTheme.colorScheme.getGreenLima // MaterialTheme.colorScheme.onPrimary // Ícono blanco si fondo es primario
                    )
                }
            },
            actions = {
                // MINI CARRITO
                /*
                MiniCart(
                    total = total,
                    totalItems = totalItems,
                    onPositioned = onPositioned,
                    modifier = modifier
                )

                 */
                
            },
            // 🔥 CLAVE: transparente
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
                navigationIconContentColor = Color.White
            ),

            modifier = modifier.fillMaxWidth()
        )
    }
}