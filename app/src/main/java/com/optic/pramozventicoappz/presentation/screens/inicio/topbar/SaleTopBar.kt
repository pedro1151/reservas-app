package com.optic.pramozventicoappz.presentation.screens.inicio.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


// ── Top bar ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleTopBar(
    onMenuClick: () -> Unit
) {


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(         Color(0xFF6D28D9))
            .drawBehind {
                drawRect(
                    color = Color.Black.copy(alpha = 0.05f)
                )
            }
    ) {
        TopAppBar(
            title = {

            },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        Icons.Default.Menu, null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            },
            actions = {


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