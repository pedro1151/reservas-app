package com.optic.pramosfootballappz.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun DefaultButtonLink(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    color: Color = Color(0xFF2196F3) // mismo color por defecto que tu DefaultButton
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            contentColor = color
        )
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = color
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

        Text(
            text = text,
            color = color
        )
    }
}
