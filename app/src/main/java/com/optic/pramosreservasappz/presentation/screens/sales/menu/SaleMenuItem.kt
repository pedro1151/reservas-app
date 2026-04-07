package com.optic.pramosreservasappz.presentation.screens.sales.menu

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.optic.pramosreservasappz.presentation.ui.theme.AmarrilloSuave

@Composable
fun SaleMenuItem(
    onClick: () -> Unit,
    title: String,
    icon: ImageVector
) {


    Row(
        modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
        Icon(
            icon,
            null,
            tint = AmarrilloSuave,
            modifier = Modifier.size(25.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF424242),
        )
    }
}
