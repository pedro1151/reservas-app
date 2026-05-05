package com.optic.pramosreservasappz.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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
    vm: AuthStateVM = hiltViewModel(),
    selectorAction: (@Composable () -> Unit)? = null,
    onMenuClick:  (() -> Unit)? = null,
) {
    val sessionData by vm.sessionData.collectAsState()

    val businessId = sessionData.businessId
    val email = sessionData.email
    val planCode = sessionData.planCode


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
            navigationIcon = {
                if (onMenuClick != null) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.13f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            },
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

                if (selectorAction != null) {
                    selectorAction()
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