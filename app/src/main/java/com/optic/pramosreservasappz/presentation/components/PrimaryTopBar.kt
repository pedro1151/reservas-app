package com.optic.pramosreservasappz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.optic.pramosreservasappz.presentation.authstate.AuthStateVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTopBar(
    title: String = "",
    navController: NavController,
    onCalendarClick: (() -> Unit)? = null,
    showTitle: Boolean = true,
    vm: AuthStateVM = hiltViewModel()
) {
    val isAuthenticated by vm.isAuthenticated.collectAsState()
    val userEmail by vm.userEmail.collectAsState()

    val gradient = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary, // azul
            MaterialTheme.colorScheme.secondary, // azul
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient)
    ) {

        TopAppBar(
            title = {
                if (showTitle) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            },
            actions = {
                if (onCalendarClick != null) {
                    IconButton(onClick = { onCalendarClick() }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Elegir fecha",
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