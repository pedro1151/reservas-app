package com.optic.pramosreservasappz.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ModernFAB(
    onClick: () -> Unit,
    contentDescription: String = "Agregar"
) {
    var pressed by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = {
            pressed = true
            onClick()
        },
        modifier = Modifier
            .size(64.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 12.dp,
            hoveredElevation = 8.dp
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        AnimatedContent(
            targetState = pressed,
            transitionSpec = {
                scaleIn(
                    animationSpec = tween(100),
                    initialScale = 0.8f
                ) + fadeIn() togetherWith
                        scaleOut(
                            animationSpec = tween(100),
                            targetScale = 1.2f
                        ) + fadeOut()
            },
            label = "fab_animation"
        ) { isPressed ->
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = contentDescription,
                modifier = Modifier.size(28.dp),
                tint = Color.White
            )
        }
    }

    LaunchedEffect(pressed) {
        if (pressed) {
            kotlinx.coroutines.delay(150)
            pressed = false
        }
    }
}
