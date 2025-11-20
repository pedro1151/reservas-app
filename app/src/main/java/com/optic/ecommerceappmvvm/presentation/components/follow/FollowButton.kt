package com.optic.ecommerceappmvvm.presentation.components.follow

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.optic.ecommerceappmvvm.presentation.ui.theme.followButtonBackground
import com.optic.ecommerceappmvvm.presentation.ui.theme.followTextColor

@Composable
fun FollowButton(
    onClick: () -> Unit = {},
    title: String = "Seguir",
    isFollowed: Boolean = true
) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(30),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary) // 👈 Contorno blanco
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.followTextColor
        )
    }
}