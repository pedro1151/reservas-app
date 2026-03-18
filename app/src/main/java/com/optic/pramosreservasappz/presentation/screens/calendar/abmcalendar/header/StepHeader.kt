package com.optic.pramosreservasappz.presentation.screens.calendar.abmcalendar.header

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.optic.pramosreservasappz.presentation.screens.calendar.components.ServiceReservationStep

@Composable
fun StepHeader(currentStep: ServiceReservationStep) {
    val progress by animateFloatAsState(
        targetValue   = (currentStep.index + 1).toFloat() / 4f,
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label         = "progress"
    )
    // Barra de progreso delgada, estilo Setmore: fondo gris claro / fill negro
    Box(
        Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color(0xFFEEEEEE))
    ) {
        Box(
            Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(Color(0xFF0D0D0D), RoundedCornerShape(1.dp))
        )
    }
}
