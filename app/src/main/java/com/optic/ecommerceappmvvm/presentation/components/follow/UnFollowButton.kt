package com.optic.ecommerceappmvvm.presentation.components.follow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.optic.ecommerceappmvvm.presentation.ui.theme.Grafito
import com.optic.ecommerceappmvvm.presentation.ui.theme.GreyLight
import com.optic.ecommerceappmvvm.presentation.ui.theme.getGreenLima
import com.optic.ecommerceappmvvm.presentation.ui.theme.getRedCardColor

@Composable
fun UnFollowButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val borderColor = MaterialTheme.colorScheme.getRedCardColor

    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(25.dp)
            .shadow(2.dp, CircleShape)
            .clip(CircleShape)
            //.background(Color.Transparent)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Default.Remove, // Ícono de "-"
            contentDescription = "Dejar de seguir",
            tint = borderColor,
            modifier = Modifier.size(20.dp)
        )
    }
}
