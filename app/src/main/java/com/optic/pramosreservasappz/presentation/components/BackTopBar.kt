package com.optic.pramosreservasappz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    title: String = "",
    navController: NavController,
    showTitle: Boolean = true,
    onClientClick: (() -> Unit)? = null,
    selectorAction: (@Composable () -> Unit)? = null
) {



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .drawBehind {
                drawRect(
                    color = Color.Black.copy(alpha = 0.05f)
                )
            }
    ) {
        TopAppBar(
            title = {
                if (showTitle) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.85f),
                        letterSpacing = 1.sp
                    )
                }
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

                if (selectorAction != null) {
                    selectorAction()
                }

                if (onClientClick != null) {
                    IconButton(onClick = { onClientClick() }) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Elegir Client",
                            tint = Color.White
                        )
                    }
                }
            },
            // 🔥 CLAVE: transparente
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
                navigationIconContentColor = Color.White
            ),

            modifier = Modifier.fillMaxWidth()
        )
    }
}