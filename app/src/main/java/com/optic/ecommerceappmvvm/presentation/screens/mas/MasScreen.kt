package com.optic.ecommerceappmvvm.presentation.screens.mas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.optic.ecommerceappmvvm.presentation.components.BackTopBar
import com.optic.ecommerceappmvvm.presentation.components.PrimaryTopBar
import com.optic.ecommerceappmvvm.presentation.screens.mas.components.MasContent
import com.optic.ecommerceappmvvm.presentation.ui.theme.GreyLight

@Composable
fun MasScreen(
    navController: NavHostController,
    isAuthenticated: Boolean
    ) {
    Scaffold (
        topBar = {
            PrimaryTopBar(
                navController = navController,
                title = "Mas"
            )
        },
        containerColor = GreyLight
    ){ paddingValues ->
        MasContent(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            isAuthenticated = isAuthenticated

        )
    }
}