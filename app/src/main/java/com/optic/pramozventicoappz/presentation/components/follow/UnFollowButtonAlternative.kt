package com.optic.pramozventicoappz.presentation.components.follow


import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun UnFollowButtonAlternative(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {


    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(25.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add, // Ícono de "+"
            contentDescription = "Dejar de seguir",
            //tint = borderColor,
            modifier = Modifier
            .size(20.dp)
            .rotate(45f)   // 👉 convierte el "+" en una "X"
        )
    }
}
